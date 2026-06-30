import requests

system_prompt = """
You are a car rental assistant.

Only answer car rental questions.
Remember the user's previous messages when answering follow-up questions.
If the question is unrelated, politely refuse.
"""

history = []
MAX_TURNS = 5

while True:
    question = input("\nUser: ")

    if question.strip().lower() == "exit":
        print("Goodbye!")
        break

    history.append({"role": "user", "content": question})

    recent_history = history[-MAX_TURNS * 2:]

    conversation_text = ""

    for message in recent_history:
        if message["role"] == "user":
            conversation_text += "\nUser: " + message["content"]
        else:
            conversation_text += "\nAssistant: " + message["content"]

    prompt = system_prompt + conversation_text + "\nAssistant:"

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

    history.append({"role": "assistant", "content": answer})


"""
User: I want to rent an SUV.
Assistant: Sure. What pickup date and return date do you need?

User: Tomorrow.
Assistant: Got it. What return date and pickup location should I use?
"""