import requests

system_prompt = """
You are a car rental assistant.

Your job is to collect the required booking information.

Required information:
- pickup date
- return date
- pickup location
- vehicle type
- number of passengers

Rules:
- If information is missing, ask one clear follow-up question.
- Do not ask for all missing fields at once unless the user has provided almost no information.
- Do not invent prices or availability.
- Only discuss car rental.
"""

history = []

while True:
    question = input("\nUser: ")

    if question.strip().lower() == "exit":
        print("Goodbye!")
        break

    history.append("User: " + question)

    prompt = system_prompt + "\n\nConversation so far:\n"
    prompt += "\n".join(history)
    prompt += "\nAssistant:"

    data = {
        "model": "llama3",
        "prompt": prompt,
        "stream": False
    }

    response = requests.post(
        "http://localhost:11434/api/generate",
        json=data
    )

    answer = response.json()["response"]

    print("Assistant:", answer)

    history.append("Assistant: " + answer)


"""
Example prompts:
1. I need a car for my family this weekend.
"""