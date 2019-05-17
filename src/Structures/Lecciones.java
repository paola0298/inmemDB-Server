package Structures;

public class Lecciones implements Comparable<Lecciones> {

    Integer grupo;
    String materia;

    @Override
    public String toString() {
        return this.grupo + " " + this.materia;
    }

    public Lecciones(Integer grupo, String materia) {
        this.grupo = grupo;
        this.materia = materia;
    }

    @Override
    public int compareTo(Lecciones that) {
        return this.grupo.compareTo(that.grupo);
    }

    public Integer getGrupo() {
        return grupo;
    }

    public void setGrupo(Integer grupo) {
        this.grupo = grupo;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }
}
