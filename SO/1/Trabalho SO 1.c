#include <pthread.h> // Biblioteca para manipulação de threads
#include <stdio.h>   // Biblioteca padrão de entrada e saída
#include <stdlib.h>  // Biblioteca para alocação de memória e geração de números aleatórios
#include <time.h>    // Biblioteca para manipulação de tempo

#define SIZE 500         // Define o tamamho da matriz
#define MAX_VALUE 1000   // Define o valor máximo de cada elemento da matriz

int matriz[SIZE][SIZE];             // Matriz de inteiros de tamanho 500x500
int soma_linhas[SIZE];              // Vetor para armazenar a soma dos elementos de cada linha da matriz

// Função de soma que será executada por cada thread
void *soma_linha(void *arg) {
  int linha = *(int *)arg;                       // Converte o argumento para o índice da linha
  soma_linhas[linha] = 0;                        // Inicializa a soma da linha com 0
  for (int i = 0; i < SIZE; i++) {               // Soma todos os elementos da linha especificada
    soma_linhas[linha] += matriz[linha][i];
  }
  pthread_exit(NULL);                            // Encerra a thread
}

int main() {
  pthread_t threads[SIZE];                 // Vetor para armazenar os identificadores das threads
  int linhas[SIZE];                        // Vetor para armazenar os índices das linhas
  srand(time(NULL));                       // Inicializa a semente do gerador de números aleatórios
  
  for (int i = 0; i < SIZE; i++) {                    // Inicializa a matriz
    for (int j = 0; j < SIZE; j++) {
      matriz[i][j] = rand() % (MAX_VALUE + 1);        // Gera um número aleatório entre 0 e 1000
    }
  }

  for (int i = 0; i < SIZE; i++) {        // Cria as threads, uma para cada linha da matriz
    linhas[i] = i;                        // Armazena o índice da linha atual
    
    // Cria uma nova thread para calcular a soma de cada linha
    if (pthread_create(&threads[i], NULL, soma_linha, &linhas[i]) != 0) {
      perror("Erro ao criar thread");     // Imprime uma mensagem de erro se a criação da thread falhar
      exit(1);                            // Encerra o programa com código de erro 1
    }
  }

  for (int i = 0; i < SIZE; i++) {                         // Espera todas as threads terminarem
    pthread_join(threads[i], NULL);             
  }

  for (int i = 0; i < SIZE; i++) {                         // Imprime as somas das linhas
    printf("Soma da linha %d: %d\n", i, soma_linhas[i]);      
  }
  
  return 0;                                                // Encerra o programa 
}