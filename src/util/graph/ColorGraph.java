package util.graph;

import util.graph.*;
import util.intset.*;
import java.util.*;
import java.io.*;

public class ColorGraph {
    public  Graph          G;
    public  int            R;
    public  int            K;
    private Stack<Integer> pile;
    public  IntSet         enleves;
    public  IntSet         deborde;
    public  int[]          couleur;
    public  Node[]         int2Node;
    static  int            NOCOLOR = -1;

    public ColorGraph(Graph G, int K, int[] phi){
	this.G       = G;
	this.K       = K;
	pile         = new Stack<Integer>(); 
	R            = G.nodeCount();
	couleur      = new int[R];
	enleves      = new IntSet(R);
	deborde       = new IntSet(R);
	int2Node     = G.nodeArray();
	for(int v=0; v < R; v++){
	    int preColor = phi[v];
	    if(preColor >= 0 && preColor < K)
		couleur[v] = phi[v];
	    else
		couleur[v] = NOCOLOR;
	}
    }

    /*-------------------------------------------------------------------------------------------------------------*/
    /* associe une couleur à tous les sommets se trouvant dans la pile */
    /*-------------------------------------------------------------------------------------------------------------*/
    
    public void selection()
    {
        while (!pile.isEmpty()) {
            int sommet = pile.pop();
            couleur[sommet] = choisisCouleur(couleursVoisins(sommet));
        }
    }


    
    /*-------------------------------------------------------------------------------------------------------------*/
    /* récupère les couleurs des voisins de t */
    /*-------------------------------------------------------------------------------------------------------------*/
    
    public IntSet couleursVoisins(int t)
    {
        IntSet colorSet = new IntSet(K);
        Node node = int2Node[t];
        NodeList voisins = node.succ();
        while( voisins!= null ){
            int voisin_key =voisins.head.mykey;
           colorSet.add( couleur[voisin_key] );

            voisins = voisins.tail;
        }
        return colorSet;
    }
    
    /*-------------------------------------------------------------------------------------------------------------*/
    /* recherche une couleur absente de colorSet */
    /*-------------------------------------------------------------------------------------------------------------*/
    
    public int choisisCouleur(IntSet colorSet)
    {
        for (int i = 0; i < colorSet.getSize(); i++)
            if (!colorSet.isMember(i)) return i;
        return NOCOLOR;
    }
    
    /*-------------------------------------------------------------------------------------------------------------*/
    /* calcule le nombre de voisins du sommet t */
    /*-------------------------------------------------------------------------------------------------------------*/
    
    public int nbVoisins(int t)
    {
        int nb = 0;
        NodeList voisins = int2Node[t].succ();
        while( voisins!= null ){
            Node voisin = voisins.head;
            if( !enleves.isMember(voisin.mykey) )
                nb++;
            voisins =voisins.tail;
        }
        return nb;
    }

    /*-------------------------------------------------------------------------------------------------------------*/
    /* simplifie le graphe d'interférence g                                                                        */
    /* la simplification consiste à enlever du graphe les temporaires qui ont moins de k voisins                   */
    /* et à les mettre dans une pile                                                                               */
    /* à la fin du processus, le graphe peut ne pas être vide, il s'agit des temporaires qui ont au moins k voisin */
    /*-------------------------------------------------------------------------------------------------------------*/

    public int simplification()
    {
        boolean ajour = true;
        while (pile.size() != R && ajour) {
            ajour = false;
            for (Node node : int2Node) {
                if (enleves.isMember(node.mykey)) continue;
                if (nbVoisins(node.mykey) < K && couleur[node.mykey] == NOCOLOR) {
                    enleves.add(node.mykey);
                    pile.push(node.mykey);
                    ajour = true;
                }
            }
        }
        return 0 ;
    }
    
    /*-------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------------------------------------------------------------------*/
    
    public void debordement()
    {
        while (pile.size() != R) {
            int sommet = choisis_sommet();
            pile.push(sommet);
            enleves.add(sommet);
            deborde.add(sommet);
            simplification();
        }

    }

    private int choisis_sommet(){
        int s;
        for(s=0;  s< this.R; s++ ){
            if( !pile.contains(s) )
                return s;
        }
        return -1;
    }


    /*-------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------------------------------------------------------------------*/

    public void coloration()
    {
	this.simplification();
	this.debordement();
	this.selection();
    }

    void affiche()
    {
	System.out.println("vertex\tcolor");
	for(int i = 0; i < R; i++){
	    System.out.println(i + "\t" + couleur[i]);
	}
    }
    
    

}
