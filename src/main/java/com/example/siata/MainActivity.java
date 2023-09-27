package com.example.siata;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText txtLatitud, txtLongitud;
    Button btnConsultar, btnEstado;
    ArrayList<PuntoMedicion> estaciones = new ArrayList();
    double A[][] = new double[20][20];
    double matrizLambdas[][] = new double[20][1];
    int PMI[][] = new int[20][1];
    double contaminacion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conectar();
        cargarDatos();
        matrizPMI();
        matrizA();
        lambdas();
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double latitud = Double.parseDouble(txtLatitud.getText().toString());
                double longitud = Double.parseDouble(txtLongitud.getText().toString());
                calcularContaminacion(latitud,longitud);
                mostrarResultado(contaminacion);
            }
        });
        //imprimirMatriz(A);
        //imprimirMatrizPMI(PMI);
    }
    private void mostrarResultado(double contaminacion){
        btnEstado.setText(contaminacion + "");
        if(contaminacion<=12){
            btnEstado.setBackgroundColor(Color.GREEN);
        }else if (contaminacion>12 && contaminacion<=35.4){
            btnEstado.setBackgroundColor(Color.YELLOW);
        }else if (contaminacion>35.4 && contaminacion<=55.4){
            btnEstado.setBackgroundColor(Color.parseColor("#744e3b"));
        }else if (contaminacion>55.4 && contaminacion<=150.4){
            btnEstado.setBackgroundColor(Color.RED);
        }else if (contaminacion>150.4 && contaminacion<=250.4){
            btnEstado.setBackgroundColor(Color.parseColor("#4C2882"));
        }else if (contaminacion>250.4){
            btnEstado.setBackgroundColor(Color.parseColor("#804000"));
        }
    }

    private void calcularContaminacion(double latitud, double longitud){
        for (int i=0; i<=matrizLambdas.length -1; i++){
            double xi = estaciones.get(i).latitud;
            double yi = estaciones.get(i).longitud;
            double r = Math.sqrt((Math.pow((latitud-xi),2)) + (Math.pow((longitud-yi),2)));
            contaminacion = matrizLambdas[i][0] * Math.pow(r,2);
        }
    }

    private double[][] lambdas(){
        double resultado [][] = new double [20][1];
        multiplicarMatriz(matrizInversa(A),PMI);
        matrizLambdas = resultado;
        return resultado;
    }

    private void imprimirMatriz(double[][] matriz){
        for(int i=0;i<=matriz.length-1;i++) {
            for (int j = 0; j <= matriz.length-1; j++) {
                System.out.println(matriz[i][j] + " ");
            }
        }
    }

    private void imprimirMatrizPMI(int[][] matriz){
        for(int i=0;i<=matriz.length-1;i++) {
            System.out.println(matriz[i][0] + " ");
        }
    }



    private void matrizA(){
        int valores = estaciones.size()-1;
        for (int i = 0; i <= valores; i++) {
            for (int j = 0; j <= valores; j++) {
                double resultado = distancia(i,j);
                A[i][j] = Math.sqrt(1 + (Math.pow(resultado,2)));
            }
        }
    }

    private double distancia(int i, int j){
        double resultado, x1,x2,y1,y2;
        x1 = estaciones.get(i).latitud;
        x2 = estaciones.get(j).latitud;
        y1 = estaciones.get(i).longitud;
        y2 = estaciones.get(j).longitud;
        resultado = Math.sqrt((Math.pow((x1-x2),2)) + (Math.pow((y1-y2),2)));
        return resultado;
    }

    private void matrizPMI(){
        int valores = estaciones.size()-1;
        for (int i = 0; i <= valores; i++){
            PMI[i][0] = estaciones.get(i).concentracion;
        }
    }

    private void cargarDatos(){
        estaciones.add(new PuntoMedicion(6.3453598,-75.5047531,11));
        estaciones.add(new PuntoMedicion(6.2525611,-75.5678024,11));
        estaciones.add(new PuntoMedicion(6.2778502,-75.6364288,18));
        estaciones.add(new PuntoMedicion(6.2849998,-75.5830536,20));
        estaciones.add(new PuntoMedicion(6.2904806,-75.5555191,20));
        estaciones.add(new PuntoMedicion(6.2687888,-75.5737076,23));
        estaciones.add(new PuntoMedicion(6.2372341,-75.610466,25));
        estaciones.add(new PuntoMedicion(6.2525611,-75.5695801,28));
        estaciones.add(new PuntoMedicion(6.2589092,-75.5482635,21));
        estaciones.add(new PuntoMedicion(6.236361,-75.4984741,10));
        estaciones.add(new PuntoMedicion(6.1684971,-75.6443558,19));
        estaciones.add(new PuntoMedicion(6.1856666,-75.5972060999999,21));
        estaciones.add(new PuntoMedicion(6.1998701,-75.5609512,18));
        estaciones.add(new PuntoMedicion(6.1555305,-75.6441727,13));
        estaciones.add(new PuntoMedicion(6.1523128,-75.6274872,23));
        estaciones.add(new PuntoMedicion(6.1455002,-75.6212616,18));
        estaciones.add(new PuntoMedicion(6.1686831,-75.5819702,18));
        estaciones.add(new PuntoMedicion(6.1825418,-75.5506362999999,18));
        estaciones.add(new PuntoMedicion(6.0930777,-75.637764,15));
        estaciones.add(new PuntoMedicion(6.3375502,-75.5678024,15));
    }

    private void conectar() {
        txtLatitud = findViewById(R.id.txtLatitud);
        txtLongitud = findViewById(R.id.txtLongitud);
        btnConsultar = findViewById(R.id.btnConsultar);
        btnEstado = findViewById(R.id.btnEstado);
    }

    public double[][] matrizInversa(double[][] matriz) {
        double det=1/determinante(matriz);
        double[][] nmatriz=matrizAdjunta(matriz);
        multiplicarMatrizEscalar(det,nmatriz);
        return nmatriz;
    }

    public void multiplicarMatrizEscalar(double n, double[][] matriz) {
        for(int i=0;i<matriz.length;i++)
            for(int j=0;j<matriz.length;j++)
                matriz[i][j]*=n;
    }

    public double[][] multiplicarMatriz(double[][] a, int[][] b) {
        double [][] c = new double[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                for (int k = 0; k < a[0].length; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return c;
    }

    public double[][] matrizAdjunta(double [][] matriz){
        return matrizTranspuesta(matrizCofactores(matriz));
    }

    public double[][] matrizCofactores(double[][] matriz){
        double[][] nm=new double[matriz.length][matriz.length];
        for(int i=0;i<matriz.length;i++) {
            for(int j=0;j<matriz.length;j++) {
                double[][] det=new double[matriz.length-1][matriz.length-1];
                double detValor;
                for(int k=0;k<matriz.length;k++) {
                    if(k!=i) {
                        for(int l=0;l<matriz.length;l++) {
                            if(l!=j) {
                                int indice1=k<i ? k : k-1 ;
                                int indice2=l<j ? l : l-1 ;
                                det[indice1][indice2]=matriz[k][l];
                            }
                        }
                    }
                }
                detValor=determinante(det);
                nm[i][j]=detValor * (double)Math.pow(-1, i+j+2);
            }
        }
        return nm;
    }

    public double[][] matrizTranspuesta(double [][] matriz){
        double[][]nuevam=new double[matriz[0].length][matriz.length];
        for(int i=0; i<matriz.length; i++)
        {
            for(int j=0; j<matriz.length; j++)
                nuevam[i][j]=matriz[j][i];
        }
        return nuevam;
    }

    public double determinante(double[][] matriz)
    {
        double det;
        if(matriz.length==2)
        {
            det=(matriz[0][0]*matriz[1][1])-(matriz[1][0]*matriz[0][1]);
            return det;
        }
        double suma=0;
        for(int i=0; i<matriz.length; i++){
            double[][] nm=new double[matriz.length-1][matriz.length-1];
            for(int j=0; j<matriz.length; j++){
                if(j!=i){
                    for(int k=1; k<matriz.length; k++){
                        int indice=-1;
                        if(j<i)
                            indice=j;
                        else if(j>i)
                            indice=j-1;
                        nm[indice][k-1]=matriz[j][k];
                    }
                }
            }
            if(i%2==0)
                suma+=matriz[i][0] * determinante(nm);
            else
                suma-=matriz[i][0] * determinante(nm);
        }
        return suma;
    }
}