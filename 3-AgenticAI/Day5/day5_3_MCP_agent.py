import asyncio
from dotenv import load_dotenv

from langchain_openai import ChatOpenAI
from langchain.agents import create_agent
from langchain_mcp_adapters.client import MultiServerMCPClient

load_dotenv()

SYSTEM_PROMPT = """
You are a data assistant. Use the database tools to answer questions.
Do not guess. Only use read-only SQL.
"""

def pretty_print(response):
    # print("\n raw response")
    # print(response)

    for m in response["messages"]:
        print("\n---", m.type, "---")
        print(m)

    last_msg = response["messages"][-1]

    if isinstance(last_msg.content, list):
        text = "".join(
            block["text"]
            for block in last_msg.content
            if block.get("type") == "text"
        )
    else:
        text = last_msg.content
    print(text)

# async def main():
async def ask_agent(user_question: str) -> str:
    client = MultiServerMCPClient(
        {
            "company_db": {
                "command": "python",
                "args": ["day5_3_MCP_server.py"],
                "transport": "stdio",
            }
        }
    )

    tools = await client.get_tools()
    print("\n -------------- List of tools--------------:\n", tools)

    llm = ChatOpenAI(
        model="gpt-4o-mini",
        temperature=0
    )

    agent = create_agent(llm, tools=tools, system_prompt=SYSTEM_PROMPT)

    result = await agent.ainvoke(
        {
            "messages": [
                {
                    "role": "user",
                    "content": user_question
                }
            ]
        }
    )

    pretty_print(result)
    return(reslut["messages"][-1].content)

# if __name__ == "__main__":
#     asyncio.run(main())