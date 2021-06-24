package it.polito.tdp.newufosightings.model;

public class Evento implements Comparable<Evento>
{
	private State s; 
	private Integer day;
	public Evento(State s, Integer day)
	{
		super();
		this.s = s;
		this.day = day;
	}
	public State getS()
	{
		return s;
	}
	public Integer getDay()
	{
		return day;
	}
	@Override public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((day == null) ? 0 : day.hashCode());
		result = prime * result + ((s == null) ? 0 : s.hashCode());
		return result;
	}
	@Override public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Evento other = (Evento) obj;
		if (day == null)
		{
			if (other.day != null) return false;
		}
		else if (!day.equals(other.day)) return false;
		if (s == null)
		{
			if (other.s != null) return false;
		}
		else if (!s.equals(other.s)) return false;
		return true;
	}
	@Override public String toString()
	{
		return String.format("giorno %d -> %s in DEFCON %d ", day,s, s.getDEFCON());
	}
	@Override public int compareTo(Evento other)
	{
		return this.day - other.day;
	}
	
}
