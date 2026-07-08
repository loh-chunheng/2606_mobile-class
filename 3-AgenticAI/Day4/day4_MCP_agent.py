import asyncio
from dotenv import load_dotenv

from langchain_openai import ChatOpenAI
from langchain.agents import create_agent
from langchain_mcp_adapters.client import MultiServerMCPClient

load_dotenv()

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

async def main():
    client = MultiServerMCPClient(
        {
            "company_db": {
                "command": "python",
                "args": ["day4_MCP_server.py"],
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

    agent = create_agent(llm, tools)

    result = await agent.ainvoke(
        {
            "messages": [
                {
                    "role": "system",
                    "content": """ You are a data assistant. Use the database tools to answer questions.
Do not guess. Only use read-only SQL.
"""
                },
                {
                    "role": "user",
                    "content": "Which department has the highest average salary?"
                }
            ]
        }
    )

    pretty_print(result)

    print("\n --------------- Final Answer -----------------\n")
    print(result["messages"][-1].content)

if __name__ == "__main__":
    asyncio.run(main())