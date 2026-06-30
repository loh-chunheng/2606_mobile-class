from pathlib import Path
import chromadb

CHROMA_PATH = Path(__file__).parent / "chroma_db"
COLLECTION_NAME = "cars_rag"

client = chromadb.PersistentClient(path=str(CHROMA_PATH))
collection = client.get_collection(COLLECTION_NAME)

data = collection.get(include=["documents", "metadatas"])

print("Collection count:", collection.count())
for i, doc in enumerate(data["documents"][:5]):
    print(f"\n--- Record {i+1} ---")
    print("ID:", data["ids"][i])
    print("Metadata:", data["metadatas"][i])
    print("Document:", doc[:300], "...")