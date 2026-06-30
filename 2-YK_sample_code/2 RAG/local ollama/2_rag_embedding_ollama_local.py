# rag_minilm_ollama.py
# pip install sentence-transformers ollama numpy

import os
from pathlib import Path
import numpy as np
from sentence_transformers import SentenceTransformer
from ollama import Client

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
DATA_FILE = Path(__file__).parent / "car rental data" / "cars.txt"
EMBED_MODEL = "all-MiniLM-L6-v2"
CHAT_MODEL = "llama3"

embedder = SentenceTransformer(EMBED_MODEL)
ollama = Client(host="http://localhost:11434")


def load_text(path):
    with open(path, "r", encoding="utf-8") as f:
        return f.read()


def chunk_text(text, chunk_size=300, overlap=50):
    words = text.split()
    chunks = []
    i = 0
    while i < len(words):
        chunks.append(" ".join(words[i:i + chunk_size]))
        i += max(1, chunk_size - overlap)
    return chunks


def embed(texts):
    return embedder.encode(texts, normalize_embeddings=True)


def retrieve(query, chunks, chunk_embeddings, top_k=4):
    q_emb = embed([query])[0]
    sims = np.dot(chunk_embeddings, q_emb)
    idx = np.argsort(sims)[::-1][:top_k]
    return [(chunks[i], float(sims[i])) for i in idx]


def answer(query, top_chunks):
    context = "\n\n".join([f"Chunk {i+1}: {chunk}" for i, (chunk, _) in enumerate(top_chunks)])

    prompt = f"""Use only the context below to answer the question.

Context:
{context}

Question: {query}

Answer:"""

    resp = ollama.generate(model=CHAT_MODEL, prompt=prompt)
    return resp["response"]


def main():
    if not os.path.exists(DATA_FILE):
        raise FileNotFoundError(f"Missing file: {DATA_FILE}")

    text = load_text(DATA_FILE)
    chunks = chunk_text(text)
    chunk_embeddings = embed(chunks)

    while True:
        query = input("\nAsk a question (or type 'exit'): ").strip()
        if query.lower() == "exit":
            break

        top_chunks = retrieve(query, chunks, chunk_embeddings, top_k=4)
        print("\nAnswer:\n")
        print(answer(query, top_chunks))


if __name__ == "__main__":
    main()