import java.util.Scanner;
public class Main {
    private static final int CAPACITE = 50;
    private static Employe[] employes = new Employe[CAPACITE];
    private static int taille = 0;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        boolean quitter = false;
        while (!quitter) {
            printMenu();
            String choix = sc.nextLine().trim();
            switch (choix) {
                case "1": ajouterEmployeInteractive(); break;
                case "2": modifierEmployeInteractive(); break;
                case "3": supprimerEmployeInteractive(); break;
                case "4": afficherEmployes(); break;
                case "5": rechercherEmployeInteractive(); break;
                case "6": System.out.printf("Masse salariale totale : %.2f%n", calculerMasseSalariale()); break;
                case "7":
                    System.out.print("Tri croissant ? (o/N) : ");
                    String r = sc.nextLine().trim().toLowerCase();
                    trierEmployesParSalaire(r.equals("o") || r.equals("oui") || r.equals("y"));
                    break;
                case "0": quitter = true; break;
                default: System.out.println("Choix invalide.");
            }
        }
        sc.close();
        System.out.println("Au revoir !");
    }

    public static void printMenu() {
        System.out.println("\n=== GESTION DES EMPLOYES ===");
        System.out.println("1. Ajouter un employé");
        System.out.println("2. Modifier un employé");
        System.out.println("3. Supprimer un employé");
        System.out.println("4. Afficher la liste des employés");
        System.out.println("5. Rechercher un employé (par nom ou poste)");
        System.out.println("6. Calculer la masse salariale");
        System.out.println("7. Trier les employés par salaire");
        System.out.println("0. Quitter");
        System.out.print("Choix : ");
    }

    // --- Opérations interactives (s'appuient sur méthodes non-interactives) ---
    private static void ajouterEmployeInteractive() {
        if (taille >= CAPACITE) {
            System.out.println("Tableau plein : impossible d'ajouter un employé.");
            return;
        }
        int id = lireEntierPositif("ID employé (entier) : ");
        if (trouverIndexParId(id) != -1) {
            System.out.println("Erreur : un employé avec cet ID existe déjà.");
            return;
        }
        System.out.print("Nom complet : ");
        String nom = sc.nextLine().trim();
        System.out.print("Poste : ");
        String poste = sc.nextLine().trim();
        double salaire = lireDoublePositif("Salaire mensuel : ");
        ajouterEmploye(new Employe(id, nom, poste, salaire));
    }

    private static void modifierEmployeInteractive() {
        int id = lireEntierPositif("ID de l'employé à modifier : ");
        int idx = trouverIndexParId(id);
        if (idx == -1) { System.out.println("Employé introuvable."); return; }
        System.out.print("Nouveau nom (laisser vide pour conserver : " + employes[idx].getNom() + ") : ");
        String nom = sc.nextLine().trim();
        if (nom.isEmpty()) nom = employes[idx].getNom();
        System.out.print("Nouveau poste (laisser vide pour conserver : " + employes[idx].getPoste() + ") : ");
        String poste = sc.nextLine().trim();
        if (poste.isEmpty()) poste = employes[idx].getPoste();
        double salaire = lireDoublePositif("Nouveau salaire (>=0) : ");
        modifierEmploye(id, nom, poste, salaire);
    }

    private static void supprimerEmployeInteractive() {
        int id = lireEntierPositif("ID de l'employé à supprimer : ");
        System.out.print("Confirmer suppression (o/N) ? ");
        String c = sc.nextLine().trim().toLowerCase();
        if (c.equals("o") || c.equals("oui") || c.equals("y")) {
            supprimerEmploye(id);
        } else {
            System.out.println("Suppression annulée.");
        }
    }

    private static void rechercherEmployeInteractive() {
        System.out.print("Critère de recherche (nom ou poste) : ");
        String critere = sc.nextLine().trim();
        rechercherEmploye(critere);
    }

    // --- Méthodes métiers (non interactives) ---
    public static boolean ajouterEmploye(Employe e) {
        if (taille >= CAPACITE) return false;
        if (trouverIndexParId(e.getId()) != -1) return false;
        employes[taille++] = e;
        System.out.println("Employé ajouté.");
        return true;
    }

    public static boolean modifierEmploye(int id, String nouveauNom, String nouveauPoste, double nouveauSalaire) {
        int idx = trouverIndexParId(id);
        if (idx == -1) return false;
        Employe emp = employes[idx];
        emp.setNom(nouveauNom);
        emp.setPoste(nouveauPoste);
        emp.setSalaire(nouveauSalaire);
        System.out.println("Employé modifié.");
        return true;
    }

    public static boolean supprimerEmploye(int id) {
        int idx = trouverIndexParId(id);
        if (idx == -1) {
            System.out.println("Employé introuvable.");
            return false;
        }
        for (int i = idx; i < taille - 1; i++) employes[i] = employes[i + 1];
        employes[--taille] = null;
        System.out.println("Employé supprimé.");
        return true;
    }

    public static void afficherEmployes() {
        if (taille == 0) {
            System.out.println("Aucun employé enregistré.");
            return;
        }
        System.out.println("\n--- Liste des employés (" + taille + ") ---");
        for (int i = 0; i < taille; i++) {
            System.out.println(employes[i].toString());
        }
    }

    public static void rechercherEmploye(String critere) {
        String c = critere.toLowerCase();
        boolean trouve = false;
        for (int i = 0; i < taille; i++) {
            if (employes[i].getNom().toLowerCase().contains(c) ||
                    employes[i].getPoste().toLowerCase().contains(c)) {
                if (!trouve) System.out.println("Résultats :");
                System.out.println(employes[i]);
                trouve = true;
            }
        }
        if (!trouve) System.out.println("Aucun employé trouvé pour : '" + critere + "'");
    }

    public static double calculerMasseSalariale() {
        double total = 0.0;
        for (int i = 0; i < taille; i++) total += employes[i].getSalaire();
        return total;
    }

    /**
     * Trie in-place le tableau des employés par salaire.
     * Utilise un tri à bulles simple sur la portion [0, taille).
     * @param ordreCroissant true => croissant, false => décroissant
     */
    public static void trierEmployesParSalaire(boolean ordreCroissant) {
        if (taille <= 1) {
            System.out.println("Rien à trier.");
            return;
        }
        // tri à bulles (stable, simple pour tableau statique petit)
        for (int pass = 0; pass < taille - 1; pass++) {
            boolean echange = false;
            for (int i = 0; i < taille - 1 - pass; i++) {
                int cmp = Employe.compareParSalaire(employes[i], employes[i + 1]);
                if ((ordreCroissant && cmp > 0) || (!ordreCroissant && cmp < 0)) {
                    Employe tmp = employes[i];
                    employes[i] = employes[i + 1];
                    employes[i + 1] = tmp;
                    echange = true;
                }
            }
            if (!echange) break;
        }
        System.out.println("Triage terminé (" + (ordreCroissant ? "croissant" : "décroissant") + ").");
        afficherEmployes();
    }

    // --- Utilitaires ---
    private static int trouverIndexParId(int id) {
        for (int i = 0; i < taille; i++) if (employes[i].getId() == id) return i;
        return -1;
    }

    private static int lireEntierPositif(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int v = Integer.parseInt(sc.nextLine().trim());
                if (v >= 0) return v;
            } catch (NumberFormatException ignored) {}
            System.out.println("Entrée invalide, veuillez saisir un entier >= 0.");
        }
    }

    private static double lireDoublePositif(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double v = Double.parseDouble(sc.nextLine().trim());
                if (v >= 0) return v;
            } catch (NumberFormatException ignored) {}
            System.out.println("Entrée invalide, veuillez saisir un nombre >= 0.");
        }
    }
}