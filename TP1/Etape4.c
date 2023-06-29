#include <stdio.h>
#include <stdlib.h>
#include <string.h>


int main(){

 FILE * f1 = NULL;
 FILE * f2 = NULL;
 int nb_propositions;
 int but[20];
 int nbut[20];
 int i;
 char nomBC[20], c;
 int nb_variables, nb_clauses;


 printf("Veuillez entrer le nom de votre Base de connaissance (sans extension) \n");
 gets(nomBC);
 strcat(nomBC,".cnf");
 f1=fopen(nomBC,"r+");

    if(f1==NULL) printf("Impossible d'ouvrir le fichier contenant la BC!\n");
    else{
            f2=fopen("fichieram.cnf","rw+");//fichier temporaire
            if(f2==NULL) printf("Impossible d'ovrire le fichier temporaire \n");
            else{
                fscanf(f1, "p cnf %d %d ",&nb_variables,&nb_clauses);
                nb_clauses = nb_clauses + 1 ;                              //incr�menter le nombre de clauses
                fprintf (f2,"p cnf %d %d  ""\n", nb_variables,nb_clauses);   //Ecriture de la ligne dans le fichier temporaire
            }
            //on copie le reste du fichier amelior dans le fichier temporaire
            c=fgetc(f1);
            while(c!=EOF){
                c=fgetc(f1);
                if(c!= EOF) fputc(c,f2);
            }

 //Inputs
 printf("Liste des variables de la base de connaissances:   \n ");
 printf("1: Na;\t\t 2: Nb;\t\t 3: Nc;\t \n 4: Cea;\t 5: Ceb:\t 6: Cec;\t \n 7: Coa;\t 8: Cob;\t 9: Coc;\t \n 10: Ma;\t 11: Mb;\t 12: Mc;\t\n\n");
 printf("Donner le nombre de proposition que contient votre but:  \n");

 scanf("%d",&nb_propositions);

 for(i=1; i<nb_propositions+1; i++){
    printf("Veuillez entrer la variable %d :  \n",i);
    scanf("%d", &but[i]);
    if(but[i]>-13 && but[i]<13) nbut[i]=but[i]*(-1); //n�gation du but
    else puts("Erreur, Vous avez entrer une variable qui n'appartient pas a la liste");

 }
 //Ajout du 0 representant la fin d une clause dans le fichier cnf
 fprintf(f2, "\n");
 for(i=1; i<nb_propositions+1; i++)fprintf(f2,"%d ",nbut[i]);  //Ajout du non but au fichier tmp
 fprintf(f2,"0");
 fclose(f2); //fermeture du fichier

 //Appel du solver pour le test
 system("ubcsat -alg saps -i fichieram.cnf -solve > resultat.txt");
 }
 fclose(f2);

 //Affichage des r�sultats:
 int quit=0;
    FILE *f =fopen("resultat.txt","r+");
    if(f1==NULL) printf("impossible d'ouvrir le fichier resultat.txt  \n");
    else{
        char texte[1000];
        while(fgets(texte, 1000, f) && !quit){
            if(strstr(texte, "# Solution found for -target 0")){
                printf("\n BC U {Non but} est satisfiable\nSolution trouvee : \n");
                fscanf(f, "\n");
                while(!strstr(fgets(texte, 1000, f), "Variables"))
                    printf("%s", texte);//la solution
                quit=1;
            }
        }
        if(quit==0){
            int k;
        for(k=1;k<nb_propositions+1;k++){
            printf("-%d ",but[k]);
        }
        if(k>2){
            printf("ne peuvent pas etre atteints");
        }
        else {printf("ne peut pas etre atteint\n");}
        }
    }

    fclose(f2);
}


