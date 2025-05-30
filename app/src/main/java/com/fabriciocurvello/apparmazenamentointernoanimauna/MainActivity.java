package com.fabriciocurvello.apparmazenamentointernoanimauna;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "dados.txt";
    private static final String TEXT_VIEW_STATE = "textViewState";
    private EditText etEntradaMsg;
    private TextView tvExibeMsg;
    private Button btSalvar;
    private Button btCarregar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etEntradaMsg = findViewById(R.id.et_entrada_msg);
        tvExibeMsg = findViewById(R.id.tv_exibe_msg);
        btSalvar = findViewById(R.id.bt_salvar);
        btCarregar = findViewById(R.id.bt_carregar);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarDados();
            }
        });

        btCarregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carregarDados();
            }
        });

    }

    private void salvarDados() {
        String mensagem = etEntradaMsg.getText().toString();
        try (FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE)) { //Cria o arquivo que vai salvar os dados
            fos.write(mensagem.getBytes()); // salva os dados no arquivo
            etEntradaMsg.setText(""); // Apaga a mensagem do EditText
            tvExibeMsg.setText("Mensagem salva com sucesso!"); // Exibe a msg no TextView
        } catch (IOException e) {
            e.printStackTrace();
            tvExibeMsg.setText("Erro ao salvar a mensagem."); // Exibe a mensagem caso aconteça um erro
        }

    }

    private void carregarDados() {
        try (FileInputStream fis = openFileInput(FILE_NAME)) { // Acesso ao arquivo com os dados
            int size = fis.available(); //busca o tamanho do dado
            byte[] buffer = new byte[size]; // cria vetor para salvar os dados
            fis.read(buffer); // executa leitura do arquivo de dados e salva no buffer
            String mensagem = new String(buffer); // carrego os dados do buffer em uma String
            tvExibeMsg.setText(mensagem); // exibir a mensagem para o usuário no TextView
        } catch ( IOException e ) {
            e.printStackTrace();
            tvExibeMsg.setText("Erro ao carregar a mensagem.");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outstate) {
        super.onSaveInstanceState(outstate);
        // Salva o estado do TextView no Bundle
        outstate.putString(TEXT_VIEW_STATE, tvExibeMsg.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restaura o estado do TexteView após a rotação
        if (savedInstanceState != null) {
            tvExibeMsg.setText(savedInstanceState.getString(TEXT_VIEW_STATE));
        }
    }

}