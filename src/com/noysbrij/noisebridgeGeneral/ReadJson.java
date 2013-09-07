package com.noysbrij.noisebridgeGeneral;
import java.io.*;
import android.util.*;
import android.widget.*;
import java.util.*;

public class ReadJson extends NoiseBridgeGeneral
{
// tickets
    Tickets tickets;
//	count the tickets
	int ticketCount;

 	public Tickets readTickets(InputStream in) throws IOException
	{
		ticketCount = -1;
		tickets = new Tickets();
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try
		{
		    reader.beginArray();
			while (reader.hasNext())
			{				
				tickets.tickets.add(new Ticket());
				readTicket(reader);
			}
			reader.endArray();
		}
		finally
		{
			reader.close();
		}
		return tickets;
	}

	public void readTicket(JsonReader reader) throws IOException
	{
    	reader.beginObject();
		while (reader.hasNext())
		{			

			String name = reader.nextName();
			if (name.equals("complexity"))
			{
                tickets.tickets.add(new Ticket());
				tickets.tickets.get(++ticketCount).complexity = reader.nextInt();
				Log.i("NBG", "user: " + tickets.tickets.get(ticketCount).id);
			}
			else if (name.equals("created_at"))
			{
				tickets.tickets.get(ticketCount).created_at = reader.nextString();
			}
			else if (name.equals("description"))
			{
				tickets.tickets.get(ticketCount).description = reader.nextString();
			}
			else if (name.equals("do_at"))
			{
				tickets.tickets.get(ticketCount).do_at = reader.nextString();
			}
			else if (name.equals("id"))
			{
				tickets.tickets.get(ticketCount).id = reader.nextInt();
			}
			else if (name.equals("owner_id"))
			{
				tickets.tickets.get(ticketCount).id = reader.nextInt();
			}
			else if (name.equals("requestor_id"))
			{
				tickets.tickets.get(ticketCount).id = reader.nextInt();
			}
			else if (name.equals("status"))
			{
				tickets.tickets.get(ticketCount).status = reader.nextString();
			}
			else if (name.equals("ticket_type"))
			{
				tickets.tickets.get(ticketCount).ticket_type = reader.nextString();
			}
			else if (name.equals("title"))
			{
				tickets.tickets.get(ticketCount).title = reader.nextString();
			}
			else if (name.equals("updated_at"))
			{
				tickets.tickets.get(ticketCount).updated_at = reader.nextString();
			}
			else
			{
				reader.skipValue();
			}
		}
		reader.endObject();
	}

}

class Tickets
{
	protected ArrayList<Ticket> tickets;
	public Tickets()
	{
		tickets = new ArrayList<Ticket>();
	}
}

class Ticket
{
	int complexity;
	String created_at;
	String description;
	String do_at;
	int id;
	int owner_id;
	int requestor_id;
	String status;
	String ticket_type;
	String title;
	String updated_at;

}
