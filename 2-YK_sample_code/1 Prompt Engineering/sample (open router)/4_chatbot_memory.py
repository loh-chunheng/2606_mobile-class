from openai import OpenAI
from dotenv import load_dotenv
import os

load_dotenv()

client = OpenAI(
    base_url="https://openrouter.ai/api/v1",
    api_key=os.getenv("OPENROUTER_API_KEY")
)

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

    messages = [{"role": "system", "content": system_prompt}] + recent_history

    response = client.chat.completions.create(
        model="meta-llama/llama-3-8b-instruct",
        messages=messages
    )

    answer = response.choices[0].message.content

    print("Assistant:", answer)

    history.append({"role": "assistant", "content": answer})


"""
User: I want to rent an SUV.
Assistant: Sure. What pickup date and return date do you need?

User: Tomorrow.
Assistant: Got it. What return date and pickup location should I use?
"""
