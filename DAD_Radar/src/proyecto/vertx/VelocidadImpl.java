package proyecto.vertx;

import java.util.Date;
import java.sql.Timestamp;

public class VelocidadImpl {

	protected Integer IdVelocidad;
	protected Double Speed;
	protected Object Fecha;
	
	public VelocidadImpl(Integer IdVelocidad, Double Speed, Object Fecha) {
		
		super();
		this.IdVelocidad = IdVelocidad;
		this.Speed = Speed;
		this.Fecha = Fecha;
		
	}
public VelocidadImpl( Double Speed, Object Fecha) {
		
		super();
		this.Speed = Speed;
		this.Fecha = Fecha;
		
	}

	public VelocidadImpl() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public Integer getIdVelocidad() {
		return IdVelocidad;
	}

	public void setIdVelocidad(Integer IdVelocidad) {
		this.IdVelocidad = IdVelocidad;
	}

	public Double getSpeed() {
		return Speed;
	}

	public void setSpeed(Double Speed) {
		this.Speed = Speed;
	}

	public Object getFecha() {
		return Fecha;
	}
	

	public void setFecha(Timestamp Fecha) {
		this.Fecha = Fecha;
	}


	@Override
	public String toString() {
		return "velocidadImpl [speed=" + Speed + ", fecha=" + Fecha + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((IdVelocidad == null) ? 0 : IdVelocidad.hashCode());
		result = prime * result + ((Speed == null) ? 0 : Speed.hashCode());
		result = prime * result + ((Fecha == null) ? 0 : Fecha.hashCode());
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VelocidadImpl other = (VelocidadImpl) obj;
		if (IdVelocidad == null) {
			if (other.IdVelocidad != null)
				return false;
		} else if (!IdVelocidad.equals(other.IdVelocidad))
			return false;
		if (Speed == null) {
			if (other.Speed != null)
				return false;
		} else if (!Speed.equals(other.Speed))
			return false;
		if (Fecha == null) {
			if (other.Fecha != null)
				return false;
		} else if (!Fecha.equals(other.Fecha))
			return false;
		
		return true;
	}

}
