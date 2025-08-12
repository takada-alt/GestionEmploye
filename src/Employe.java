public class Employe {
    private int id;
    private String nom;
    private String poste;
    private double salaire;

    public Employe() {
        this(0, "", "", 0.0);
    }

    public Employe(int id, String nom, String poste, double salaire) {
        this.id = id;
        this.nom = nom;
        this.poste = poste;
        this.salaire = salaire;
    }

    // Getters / Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPoste() { return poste; }
    public void setPoste(String poste) { this.poste = poste; }

    public double getSalaire() { return salaire; }
    public void setSalaire(double salaire) { this.salaire = salaire; }

    // Représentation textuelle
    @Override
    public String toString() {
        return String.format("ID: %d | Nom: %s | Poste: %s | Salaire: %.2f", id, nom, poste, salaire);
    }

    // Méthode statique de comparaison par salaire (utile pour le tri)
    public static int compareParSalaire(Employe a, Employe b) {
        return Double.compare(a.getSalaire(), b.getSalaire());
    }
}