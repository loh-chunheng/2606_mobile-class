import requests

system_prompt = """
You are a travel planning assistant.

Only answer travel itinerary planning questions.
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

    history.append({"role": "user", "content": question}) # append a dictionary to the list with the role and content of the user's message
    recent_history = history[-MAX_TURNS * 2:]

    conversation_text = ""

    for message in recent_history:
        conversation_text += f"{message['role'].capitalize()}: {message['content']}\n"  
    
    prompt = system_prompt + conversation_text + "Assistant:"

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

    history.append({"role": "assistant", "content": answer}) # append a dictionary to the list with the role and content of the assistant's message 
