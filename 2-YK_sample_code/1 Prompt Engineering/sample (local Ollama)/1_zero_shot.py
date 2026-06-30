import requests

question = input("Ask the chatbot: ")

data = {
    "model": "llama3",
    "prompt": question,
    "stream": False
}

response = requests.post(
    "http://localhost:11434/api/generate",
    json=data
)

print(response.json()["response"])

"""
Example prompts:
1. Recommend a car for a family of 5.
2. Write a polite message to a customer who wants to cancel a booking.
3. What is the capital of France?
"""