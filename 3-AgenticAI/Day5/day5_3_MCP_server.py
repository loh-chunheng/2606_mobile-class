from mcp.server.fastmcp import FastMCP
import psycopg2

mcp = FastMCP("Company Database")

DB_CONFIG = dict(
    host="localhost",
    port=5432,
    database="postgres",
    user="postgres",
    password="postgres"
)

def get_conn():
    return psycopg2.connect(**DB_CONFIG)

@mcp.tool()
def list_tables():
    """List all tables."""
    conn = get_conn()
    cur = conn.cursor()

    cur.execute("""
    SELECT table_name
    FROM information_schema.tables
    WHERE table_schema='public'
    ORDER BY table_name;
    """)

    rows = [r[0] for r in cur.fetchall()]

    cur.close()
    conn.close()

    return rows

@mcp.tool()
def describe_table(table_name:str):
    """Describe a table."""

    conn = get_conn()
    cur = conn.cursor()

    cur.execute("""
    SELECT column_name,data_type
    FROM information_schema.columns
    WHERE table_name=%s
    ORDER BY ordinal_position
    """,(table_name,))

    rows = [
        {
            "column":r[0],
            "type":r[1]
        }
        for r in cur.fetchall()
    ]

    cur.close()
    conn.close()

    return rows

@mcp.tool()
def run_readonly_query(sql:str):
    """
    Execute a SELECT statement.
    """

    if not sql.lower().strip().startswith("select"):
        raise ValueError("Only SELECT statements allowed.")

    conn = get_conn()
    cur = conn.cursor()
    cur.execute(sql)

    columns = [d[0] for d in cur.description]

    results = []

    for row in cur.fetchall():
        results.append(
            dict(zip(columns,row))
        )

    cur.close()
    conn.close()

    return results

if __name__=="__main__":
    mcp.run(transport="stdio")
    #mcp.run(transport="http", port=8000)