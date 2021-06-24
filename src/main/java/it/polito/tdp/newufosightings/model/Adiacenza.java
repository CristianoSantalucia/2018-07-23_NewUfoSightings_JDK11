package it.polito.tdp.newufosightings.model;
/*
 * classe Adiacenza preimpostata 
 * questo documento è soggetto ai relativi diritti di ©Copyright
 * giugno 2021
 */ 

public class Adiacenza
{
	private State a1;
	private State a2; 
	private Integer peso;

	public Adiacenza(State a1, State a2, Integer peso)
	{ 
		this.a1 = a1;
		this.a2 = a2;
		this.peso = peso;
	}
	public State getA1()
	{
		return a1;
	}
	public void setC1(State a1)
	{
		this.a1 = a1;
	}
	public State getA2()
	{
		return a2;
	}
	public void setC2(State a2)
	{
		this.a2 = a2;
	}
	public Integer getPeso()
	{
		return peso;
	}
	public void setPeso(Integer peso)
	{
		this.peso = peso;
	}
	@Override public String toString()
	{
		return String.format("%s - %s (%d)", a1, a2, peso);
	}  
}
