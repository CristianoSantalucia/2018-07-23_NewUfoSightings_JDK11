package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.alg.cycle.HierholzerEulerianCycle;

import it.polito.tdp.newufosightings.model.Adiacenza;
import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;

public class NewUfoSightingsDAO
{
	public List<Sighting> loadAllSightings(int year)
	{
		String sql = "SELECT * FROM sighting "
					+ "WHERE YEAR(sighting.datetime) = ? "
					+ "ORDER BY sighting.datetime ";
		List<Sighting> list = new ArrayList<>();

		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			ResultSet res = st.executeQuery();

			while (res.next())
			{
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public void getVertici(Map<String, State> vertici)
	{
		String sql = "SELECT * FROM state";

		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next())
			{
				State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				vertici.putIfAbsent(state.getId(), state);
			}

			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<String> getShapes(int year)
	{
		String sql = "SELECT s.shape shape " + "FROM sighting s " + "WHERE YEAR(s.datetime) = ? "
				+ "		AND s.shape <> '' " + "GROUP BY shape";

		List<String> result = new ArrayList<>();

		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			ResultSet rs = st.executeQuery();

			while (rs.next())
			{
				result.add(rs.getString("shape"));
			}

			conn.close();
			return result;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Adiacenza> getAdiacenze(Map<String, State> vertici, String shape, int year)
	{
		String sql = "SELECT n1.state1 id1, n2.state2 id2, COUNT(*) peso "
					+ "FROM neighbor n1, neighbor n2, sighting s1, sighting s2 "
					+ "WHERE s1.id < s2.id "
					+ "		AND s1.state = n1.state1 "
					+ "		AND s2.state = n2.state2 "
					+ "		AND n1.state1 < n2.state2 "
					+ "		AND s1.shape = ? "
					+ "		AND s2.shape = ? "
					+ "		AND YEAR (s1.datetime) = ? "
					+ "		AND YEAR (s2.datetime) = ? "
					+ "		AND n1.state2 = n2.state1 "
					+ "		AND n2.state1 = n1.state2 "
					+ "GROUP BY n1.state1, n2.state2 ";
		
		List<Adiacenza> result = new ArrayList<>();
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, shape);
			st.setString(2, shape);
			st.setInt(3, year);
			st.setInt(4, year);
			ResultSet rs = st.executeQuery();

			while (rs.next())
			{
				State s1 = vertici.get(rs.getString("id1"));  
				State s2 = vertici.get(rs.getString("id2"));  
				Integer peso = rs.getInt("peso");
//				if (s1 != null && s2 != null)
//				{
					Adiacenza a = new Adiacenza(s1, s2, peso); 
					result.add(a);
//				}
			}

			conn.close();
			return result; 
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}
