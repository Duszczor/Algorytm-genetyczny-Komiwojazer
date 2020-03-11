import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 01.03.2018.
 */
public class ParaListaWartosc {

    public List<Integer> lista = new ArrayList<Integer>();
    public int wartosc;

    public ParaListaWartosc(List<Integer> nowaLista, int nowaWartosc){
        lista = nowaLista;
        wartosc = nowaWartosc;
    }

    public void wyswietlaniePar(){

        System.out.println("Wyswietlanie pary: ");
            System.out.println("Lista: "+lista+" Wartosc funkcji celu: "+wartosc);
    }

    public boolean czyTaSamaLista(List<Integer> listaSprawdzana){ //CZy dobrze ??
        if(lista.equals(listaSprawdzana)){
            return true;
        }
        else{
            return false;
        }
    }
}
