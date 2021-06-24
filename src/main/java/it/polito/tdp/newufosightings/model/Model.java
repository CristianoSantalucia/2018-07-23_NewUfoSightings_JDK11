package it.polito.tdp.newufosightings.model;

/*
 * classe Model preimpostata questo documento è soggetto ai relativi diritti di
 * ©Copyright giugno 2021
 */

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.*;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model
{
	private NewUfoSightingsDAO dao;
	private Map<String, State> vertici;
	private Graph<State, DefaultWeightedEdge> grafo;

	public Model()
	{
		this.dao = new NewUfoSightingsDAO();
	}

	public Collection<String> getShapes(Integer year)
	{
		return this.dao.getShapes(year);
	}

	public void creaGrafo(String shape, int year)
	{
		// ripulisco mappa e grafo
		this.vertici = new HashMap<>();
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class); //

		// vertici
		this.dao.getVertici(vertici); // riempio la mappa
		Graphs.addAllVertices(this.grafo, this.vertici.values());

		// archi
		List<Adiacenza> adiacenze = new ArrayList<>(this.dao.getAdiacenze(vertici, shape, year));
		for (Adiacenza a : adiacenze)
		{
			Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), (double) a.getPeso());
		}
	}

	public int getNumVertici()
	{
		return this.grafo.vertexSet().size();
	}

	public int getNumArchi()
	{
		return this.grafo.edgeSet().size();
	}

	public Collection<Object> getVertici()
	{
//		List<Object> vertici = new ArrayList<>(this.grafo.vertexSet());
//		return vertici;
		return null;
	}

	public Collection<DefaultWeightedEdge> getArchi()
	{
		return this.grafo.edgeSet();
	}

	public String stampaArchi()
	{
		List<State> vertici = new ArrayList<>(this.grafo.vertexSet());
		vertici.sort((s1, s2) -> s1.getName().compareTo(s2.getName()));
		String s = "";
		for (State state : vertici)
		{
			double sum = 0;
			for (State ad : Graphs.neighborListOf(this.grafo, state))
				sum += this.grafo.getEdgeWeight(this.grafo.getEdge(ad, state));
			s += String.format("\n %s (%.2f)", state, sum);
		}
		return s;
	}

	// SIMULAZIONE

	List<State> stati;
	int T1;
	int alfa;

	public void simula(int T1, int alfa, Integer year)
	{
		this.T1 = T1;
		this.alfa = alfa; 
		
		for (State s : this.grafo.vertexSet())
		{
			for (int i = 0; i < T1; i++)
			{
				for (Sighting sight : this.dao.loadAllSightings(year))
				{
					State s2 = vertici.get(sight.getState().toUpperCase());
					if (s.equals(s2))
					{
						if (s.getDEFCON() > 1) 
						{
							s.diminuisciDefcon(1);
							System.out.println("DIMINUISCO "+ s + " di 1");
						}

						int prob = (int) (Math.random() * 100);
						if (prob < alfa)
						{
							for (State adiacente : Graphs.neighborListOf(this.grafo, s))
							{
								if (adiacente.getDEFCON() > .5)
								{
									adiacente.diminuisciDefcon(.5);
									System.out.println("DIMINUISCO "+ adiacente + " di .5");
								}
							}
						}
					}
				}
			}
			if (s.getDEFCON() < 4.5) s.aumentaDefcon(1);
			for (State adiacente : Graphs.neighborListOf(this.grafo, s))
			{
				if (adiacente.getDEFCON() < 4.5) adiacente.aumentaDefcon(.5);
			}
		}

		for (State s : this.grafo.vertexSet())
		{
			System.out.println(s.toStringDefcon());
		}
	}

}
