import requests
import json

system_prompt = """
You are a helpful travel planner.

Return ONLY valid JSON.
Do not use markdown.
Do not add any extra text.

If the user input is not related to travel, return:
{"error": "Sorry, I can only assist with travel-related enquiries."}

If the input is travel-related, return JSON like this.
# Use "Singapore" as the default destination example (common practice).
{
  "destination": "Singapore",  
  "trip_length_days": 3,  
  "budget": "$1000",
  "daily_itinerary": [
    {
      "day": 1,
      "morning": "Visit Marina Bay Sands",
      "afternoon": "Explore Gardens by the Bay",
      "evening": "Dinner at Lau Pa Sat",
      "food_recommendations": ["chicken rice", "satay"]
    }
  ]
}
"""

question = input("User: ")
prompt = system_prompt + "\n\nUser message:\n" + question

data = {
    "model": "llama3",
    "prompt": prompt,
    "stream": False
}

response = requests.post("http://localhost:11434/api/generate", json=data)
answer = response.json()["response"]

print("\nRaw model output:")
print(answer)

try:
    returned_data = json.loads(answer)
    print("\nParsed Python dictionary:")
    print(returned_data)
except json.JSONDecodeError:
    print("\nError: Model did not return valid JSON.")


"""
Example prompt:
Give me a 3 days itinerary for touring in Singapore.  My budget is $1000

I need to go for a holiday
"""