# rag_minilm_ollama_chromadb.py
# pip install sentence-transformers ollama chromadb

import os
from pathlib import Path
from typing import List, Tuple, Dict, Any

import chromadb
from sentence_transformers import SentenceTransformer
from ollama import Client

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
DATA_FILE = Path(__file__).parent / "car rental data" / "cars.txt"
EMBED_MODEL = "all-MiniLM-L6-v2"
CHAT_MODEL = "llama3"

CHROMA_PATH = Path(__file__).parent / "chroma_db"
COLLECTION_NAME = "cars_rag"

embedder = SentenceTransformer(EMBED_MODEL)
ollama = Client(host="http://localhost:11434")

# Persistent local Chroma database
chroma_client = chromadb.PersistentClient(path=str(CHROMA_PATH))


def load_text(path: Path) -> str:
    with open(path, "r", encoding="utf-8") as f:
        return f.read()


def chunk_text(text: str, chunk_size: int = 300, overlap: int = 50) -> List[str]:
    words = text.split()
    chunks = []
    i = 0

    while i < len(words):
        chunks.append(" ".join(words[i:i + chunk_size]))
        i += max(1, chunk_size - overlap)

    return chunks


def embed(texts: List[str]) -> List[List[float]]:
    # Chroma expects JSON-serializable embeddings, so convert to list
    return embedder.encode(texts, normalize_embeddings=True).tolist()


def reset_collection_if_exists(name: str) -> None:
    try:
        chroma_client.delete_collection(name)
    except Exception:
        pass


def ingest_to_chroma(file_path: Path, reset: bool = True):
    text = load_text(file_path)
    chunks = chunk_text(text)

    if reset:
        reset_collection_if_exists(COLLECTION_NAME)

    collection = chroma_client.get_or_create_collection(name=COLLECTION_NAME)

    embeddings = embed(chunks)
    ids = [f"chunk_{i}" for i in range(len(chunks))]
    metadatas = [
        {
            "source": str(file_path),
            "chunk_index": i,
            "word_count": len(chunks[i].split()),
        }
        for i in range(len(chunks))
    ]

    collection.add(
        ids=ids,
        documents=chunks,
        embeddings=embeddings,
        metadatas=metadatas,
    )

    return collection, len(chunks)


def retrieve(query: str, collection, top_k: int = 4) -> List[Tuple[str, float, Dict[str, Any]]]:
    q_emb = embed([query])[0]

    results = collection.query(
        query_embeddings=[q_emb],
        n_results=top_k,
        include=["documents", "distances", "metadatas"],
    )

    documents = results["documents"][0]
    distances = results["distances"][0]
    metadatas = results["metadatas"][0]

    return list(zip(documents, distances, metadatas))


def answer(query: str, top_chunks: List[Tuple[str, float, Dict[str, Any]]]) -> str:
    context = "\n\n".join(
        [
            f"Chunk {i+1} (chunk_index={meta['chunk_index']}, distance={dist:.4f}): {chunk}"
            for i, (chunk, dist, meta) in enumerate(top_chunks)
        ]
    )

    prompt = f"""Use only the context below to answer the question.

Context:
{context}

Question: {query}

If the answer is not in the context, say "I don't know based on the provided context."

Answer:"""

    resp = ollama.generate(
        model=CHAT_MODEL,
        prompt=prompt,
    )
    return resp["response"]


def main():
    if not os.path.exists(DATA_FILE):
        raise FileNotFoundError(f"Missing file: {DATA_FILE}")

    collection, num_chunks = ingest_to_chroma(DATA_FILE, reset=True)
    print(f"Loaded {num_chunks} chunks into ChromaDB")
    print(f"Collection: {COLLECTION_NAME}")
    print(f"DB path: {CHROMA_PATH}")

    while True:
        query = input("\nAsk a question (or type 'exit'): ").strip()
        if query.lower() == "exit":
            break

        top_chunks = retrieve(query, collection, top_k=4)

        print("\nTop retrieved chunks:\n")
        for i, (chunk, dist, meta) in enumerate(top_chunks, start=1):
            preview = chunk[:140].replace("\n", " ")
            print(f"{i}. chunk_index={meta['chunk_index']} | distance={dist:.4f} | {preview}...")

        print("\nAnswer:\n")
        print(answer(query, top_chunks))


if __name__ == "__main__":
    main()