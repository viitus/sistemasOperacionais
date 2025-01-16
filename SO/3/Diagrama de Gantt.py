import random
import pandas as pd
import matplotlib.pyplot as plt

# Função para gerar processos com tempo de chegada, duração da CPU e prioridade
def gerar_processos(n):
    processos = []
    for i in range(n):
        tempo_chegada = random.randint(0, 15)
        duracao_cpu = random.randint(2, 10)
        prioridade = random.randint(1, 9)
        processos.append([i+1, tempo_chegada, duracao_cpu, prioridade])
    return processos

# Função para exibir os processos
def exibir_processos(processos):
    print("Processo\tChegada\tDuração CPU\tPrioridade")
    for p in processos:
        print(f"P{p[0]}\t\t{p[1]}\t{p[2]}\t\t{p[3]}")

# Algoritmo FCFS (First Come First Serve)
def escalonamento_fcfs(processos):
    processos.sort(key=lambda x: x[1])  # Ordena pelo tempo de chegada
    diagrama_gantt = []
    tempo = 0
    tempo_espera_total = 0
    tempo_retorno_total = 0
    for p in processos:
        if tempo < p[1]:
            tempo = p[1]
        tempo_inicio = tempo
        tempo += p[2]
        tempo_fim = tempo
        tempo_retorno = tempo_fim - p[1]
        tempo_espera = tempo_inicio - p[1]
        tempo_espera_total += tempo_espera
        tempo_retorno_total += tempo_retorno
        diagrama_gantt.append((p[0], tempo_inicio, tempo_fim))
    tempo_espera_medio = tempo_espera_total / len(processos)
    tempo_retorno_medio = tempo_retorno_total / len(processos)
    return diagrama_gantt, tempo_espera_medio, tempo_retorno_medio

# Algoritmo SJF (Shortest Job First) Não preemptivo
def escalonamento_sjf(processos):
    processos.sort(key=lambda x: (x[1], x[2]))  # Ordena por tempo de chegada e duração da CPU
    diagrama_gantt = []
    tempo = 0
    tempo_espera_total = 0
    tempo_retorno_total = 0
    fila_prontos = []
    while processos or fila_prontos:
        while processos and processos[0][1] <= tempo:
            fila_prontos.append(processos.pop(0))
        fila_prontos.sort(key=lambda x: x[2])  # Ordena pela duração da CPU
        if fila_prontos:
            p = fila_prontos.pop(0)
            tempo_inicio = tempo
            tempo += p[2]
            tempo_fim = tempo
            tempo_retorno = tempo_fim - p[1]
            tempo_espera = tempo_inicio - p[1]
            tempo_espera_total += tempo_espera
            tempo_retorno_total += tempo_retorno
            diagrama_gantt.append((p[0], tempo_inicio, tempo_fim))
        else:
            tempo += 1
    tempo_espera_medio = tempo_espera_total / len(diagrama_gantt)
    tempo_retorno_medio = tempo_retorno_total / len(diagrama_gantt)
    return diagrama_gantt, tempo_espera_medio, tempo_retorno_medio

# Algoritmo Round Robin (Quantum = 1)
def escalonamento_round_robin(processos, quantum=1):
    processos.sort(key=lambda x: x[1])  # Ordena pelo tempo de chegada
    diagrama_gantt = []
    tempo = 0
    tempo_espera_total = 0
    tempo_retorno_total = 0
    fila_prontos = []
    burst_restante = {p[0]: p[2] for p in processos}
    while processos or fila_prontos:
        while processos and processos[0][1] <= tempo:
            fila_prontos.append(processos.pop(0))
        if fila_prontos:
            p = fila_prontos.pop(0)
            tempo_inicio = tempo
            tempo_exec = min(quantum, burst_restante[p[0]])
            tempo += tempo_exec
            burst_restante[p[0]] -= tempo_exec
            diagrama_gantt.append((p[0], tempo_inicio, tempo))
            if burst_restante[p[0]] > 0:
                fila_prontos.append(p)
            else:
                tempo_fim = tempo
                tempo_retorno = tempo_fim - p[1]
                tempo_espera = tempo_retorno - p[2]
                tempo_espera_total += tempo_espera
                tempo_retorno_total += tempo_retorno
        else:
            tempo += 1
    tempo_espera_medio = tempo_espera_total / len(burst_restante)
    tempo_retorno_medio = tempo_retorno_total / len(burst_restante)
    return diagrama_gantt, tempo_espera_medio, tempo_retorno_medio

# Algoritmo de Prioridade Preemptivo
def escalonamento_prioridade(processos):
    processos.sort(key=lambda x: (x[1], x[3]))  # Ordena pelo tempo de chegada e prioridade
    diagrama_gantt = []
    tempo = 0
    tempo_espera_total = 0
    tempo_retorno_total = 0
    fila_prontos = []
    while processos or fila_prontos:
        while processos and processos[0][1] <= tempo:
            fila_prontos.append(processos.pop(0))
        fila_prontos.sort(key=lambda x: x[3])  # Ordena pela prioridade (menor valor = maior prioridade)
        if fila_prontos:
            p = fila_prontos.pop(0)
            tempo_inicio = tempo
            tempo += p[2]
            tempo_fim = tempo
            tempo_retorno = tempo_fim - p[1]
            tempo_espera = tempo_inicio - p[1]
            tempo_espera_total += tempo_espera
            tempo_retorno_total += tempo_retorno
            diagrama_gantt.append((p[0], tempo_inicio, tempo_fim))
        else:
            tempo += 1
    tempo_espera_medio = tempo_espera_total / len(diagrama_gantt)
    tempo_retorno_medio = tempo_retorno_total / len(diagrama_gantt)
    return diagrama_gantt, tempo_espera_medio, tempo_retorno_medio

# Função para exibir o diagrama de Gantt
def exibir_diagrama_gantt(diagrama_gantt, titulo):
    df = pd.DataFrame(diagrama_gantt, columns=['Processo', 'Tempo Início', 'Tempo Fim'])
    df['Duração'] = df['Tempo Fim'] - df['Tempo Início']
    plt.figure(figsize=(10, 6))
    plt.barh(df['Processo'], df['Duração'], left=df['Tempo Início'])
    plt.xlabel('Tempo')
    plt.ylabel('Processo')
    plt.title(titulo)
    plt.show()

# Função principal
def main():
    n = 20
    processos = gerar_processos(n)
    exibir_processos(processos)
    
    print("\nEscalonamento FCFS:")
    fcfs_gantt, fcfs_tempo_espera_medio, fcfs_tempo_retorno_medio = escalonamento_fcfs(processos.copy())
    exibir_diagrama_gantt(fcfs_gantt, "Escalonamento FCFS")
    print(f"Tempo Médio de Espera: {fcfs_tempo_espera_medio}")
    print(f"Tempo Médio de Retorno: {fcfs_tempo_retorno_medio}")
    
    print("\nEscalonamento SJF:")
    sjf_gantt, sjf_tempo_espera_medio, sjf_tempo_retorno_medio = escalonamento_sjf(processos.copy())
    exibir_diagrama_gantt(sjf_gantt, "Escalonamento SJF")
    print(f"Tempo Médio de Espera: {sjf_tempo_espera_medio}")
    print(f"Tempo Médio de Retorno: {sjf_tempo_retorno_medio}")
    
    print("\nEscalonamento Round Robin:")
    rr_gantt, rr_tempo_espera_medio, rr_tempo_retorno_medio = escalonamento_round_robin(processos.copy())
    exibir_diagrama_gantt(rr_gantt, "Escalonamento Round Robin (Quantum = 1)")
    print(f"Tempo Médio de Espera: {rr_tempo_espera_medio}")
    print(f"Tempo Médio de Retorno: {rr_tempo_retorno_medio}")
    
    print("\nEscalonamento por Prioridade:")
    prioridade_gantt, prioridade_tempo_espera_medio, prioridade_tempo_retorno_medio = escalonamento_prioridade(processos.copy())
    exibir_diagrama_gantt(prioridade_gantt, "Escalonamento por Prioridade")
    print(f"Tempo Médio de Espera: {prioridade_tempo_espera_medio}")
    print(f"Tempo Médio de Retorno: {prioridade_tempo_retorno_medio}")

if __name__ == "__main__":
    main()
