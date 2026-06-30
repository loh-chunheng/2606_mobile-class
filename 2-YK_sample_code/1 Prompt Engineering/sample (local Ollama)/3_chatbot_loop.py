import requests

system_prompt = """
You are a car rental assistant.

Only answer questions related to car rental.
If the question is unrelated, reply:
"Sorry, I can only assist with car rental related enquiries."
"""

while True:
    question = input("\nUser: ")

    if question.strip().lower() == "exit":
        print("Goodbye!")
        break

    prompt = system_prompt + "\nUser: " + question + "\nAssistant:"

    data = {
        "model": "llama3",
        "prompt": prompt,
        "stream": False
    }

    response = requests.post(
        "http://localhost:11434/api/generate",
        json=data
    )

    print("Assistant:", response.json()["response"])

