"""
04_structured_rag.py
 
Goal:
Show a simple structured RAG demo:
1. Load the structured Excel table.
2. Put the table directly into the prompt.
3. Ask an LLM through OpenRouter.
 
Run:
    python 04_structured_rag.py
"""
 
import os
from pathlib import Path
 
import pandas as pd
from dotenv import load_dotenv
from openai import OpenAI
 
DATA_FILE = Path(__file__).parent / "car rental data" / "structure.xlsx"
MODEL = "openai/gpt-4o-mini"
 
load_dotenv()
 
llm_client = OpenAI(
    api_key=os.getenv("OPENROUTER_API_KEY"),
    base_url="https://openrouter.ai/api/v1",
)
 
def load_cars():
    return pd.read_excel(DATA_FILE).to_string(index=False)
 
def build_prompt(question, cars_table):
    return f"""
            You are a helpful car rental chatbot.
 
            Answer only using the structured car table below.
            Use the columns for exact constraints like seats, price, and availability.
            If the table does not contain the answer, say:
            "I don't know based on the provided dataset."
 
            Structured car table from structure.xlsx:
            {cars_table}
 
            User question:
            {question}
            """.strip()
 
def call_llm(prompt):
    response = llm_client.chat.completions.create(
        model=MODEL,
        messages=[
            {"role": "system", "content": "Answer using only the provided car rental table."},
            {"role": "user", "content": prompt},
        ],
        temperature=0.2,
    )
    return response.choices[0].message.content
 
cars_table = load_cars()
question = "Which available car has at least 7 seats, and how much does it cost per day?"
prompt = build_prompt(question, cars_table)
 
print("Question:", question)
print()
print(call_llm(prompt))