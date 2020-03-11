#

# Algorytmy genetyczne

1. 1.Kwadratowy problem przydziału

Kwadratowy problem przydziału polega na wskazaniu lokalizacji dla N fabryk, tak aby zminimalizować koszt transportu (flow) pomiędzy nimi. Znając macierz odległości pomiędzy lokalizacjami i macierz wymaganego przepływu pomiędzy fabrykami A i B możemy obliczyć koszt transportu, czyli sumę iloczynów odległości między obiektami i strumieniami towarów przepływających między tymi obiektami.

1. 2.Algorytmy genetyczne

Rodzaj heurystyki przeszukującej przestrzeń alternatywnych rozwiązań w celu wyszukiwania rozwiązań najlepszych.

Sposób działania algorytmów genetycznych nieprzypadkowo przypomina zjawisko ewolucji biologicznej, ponieważ ich twórca John Henry Holland właśnie z biologii czerpał inspiracje do swoich prac. Obecnie zalicza się go do grupy algorytmów ewolucyjnych.



**Losowanie populacji:** Po ustaleniu wielkości populacji należy stworzyć wszystkie osobniki. Ze względu na fakt, że początkowa populacja powinna być jak najbardziej różnorodna każdy osobnik powinien być tworzony całkowicie losowo.

**Krzyżowanie:** W moim przypadku polega na korzystaniu metody RMX. Po selekcji wybieram liczbę osobników równej połowie ustalonej populacji i uzupełniam ją dziećmi utworzonymi właśnie z krzyżowania tą metodą.

**Selekcja:** Krok ten jest esencją całej genetyki. W tym miejscu tworzona jest nowa populacja na podstawie już istniejącej. W zależności od wartości funkcji dany osobnik ma większe (gdy jest &#39;dobry&#39;) lub mniejsze (gdy jest &#39;słaby&#39;) szanse na znalezienie się w kolejnym pokoleniu.

**Mutacja:** W moim przypadku mutacja polegała na zamianie w indeksów listy miejscami, co powoduje zmianę osobnika.

W moim rozwiązaniu skorzystałem z dwóch metod selekcji:

**Ruletka:** Selekcja w moim przypadku polega na przypisaniu przedziału wartości każdemu osobnikowi w zależności od wartości jego funkcji celu. Korzystałem ze wzoru : (Wartość funkcji najgorszego osobnika – wartość funkcji aktualnego osobnika) + 1, ponieważ w naszym przypadku zadanie polega na minimalizacji funkcji celu. Każdy osobnik przyjmował minimalną i maksymalną wartość na podstawie tego wzoru i losowałem liczbę i sprawdzałem dla którego osobnika ona pasuje. Niestety wartości te bardzo się od siebie nie różnią, więc selekcja działa prawie jak losowa.

**Turniej:** Metoda jest zupełnie różna od powyższej i polega na losowym wyborze z całej populacji kilku osobników (jest to tzw. grupa turniejowa), a później z tej grupy wybierany jest osobnik najlepiej przystosowany i on przepisywany jest do nowo tworzonej populacji. Losowanie grup turniejowych oraz wybieranie z nich najlepszego osobnika należy powtórzyć aż do utworzenia całej nowej populacji.

# Wnioski

Algorytm genetyczny radzi sobie o wiele lepiej niż te 2 nieewolucyjne ( Random i Greedy ). Wyniki otrzymany przez algorytm losowy uzyskiwał zadowalające mnie wyniki i były one zdecydowanie lepsze, niż wyniki uzyskane przez algorytm zachłanny. Parametr który mogliśmy regulować w losowym był liczba prób a algorytm greedy był u mnie bezparametrowy.

W algorytmie GA można dojść do kilku wniosków na podstawie sprawdzonych parametrów.

Liczba Pokoleń powinna być ustawiona na odpowiednią ilość, ponieważ zbyt mała powoduje otrzymanie nie zadawalającego wyniku a zbyt duża powoduje, że po pewnym pokoleniu wyniki oscylują w podobnych wartościach.

Rozmiar populacji musi  być na tyle wielki, żeby zagwarantować różnorodność osobników, przy czym nie może być on za duży, aby nie wydłużał nam naszego procesu znajdywania optimum.

Prawdopodobieństwo mutacji powinno być na takim poziomie aby nie osobniki nie utknęły w pewnej wartości funkcji celu, ale też aby ciągle ono nie występowało, żeby nie zachowywało się jak ciągłe losowanie wartości.