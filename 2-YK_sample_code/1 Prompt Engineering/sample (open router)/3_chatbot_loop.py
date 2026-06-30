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

Only answer questions related to car rental.
If the question is unrelated, reply:
"Sorry, I can only assist with car rental related enquiries."
"""

while True:
    question = input("\nUser: ")

    if question.strip().lower() == "exit":
        print("Goodbye!")
        break

    response = client.chat.completions.create(
        model="meta-llama/llama-3-8b-instruct",
        messages=[
            {"role": "system", "content": system_prompt},
            {"role": "user", "content": question}
        ]
    )

    print("Assistant:", response.choices[0].message.content)
