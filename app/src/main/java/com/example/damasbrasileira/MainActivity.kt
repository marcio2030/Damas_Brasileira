package com.example.damasbrasileira

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

// Um programa simples de jogo de damas brasileiro em kotlin
// Cada peça é representada por um caractere: 'b' para brancas, 'p' para pretas, 'B' para damas brancas e 'P' para damas pretas
// O tabuleiro é uma matriz de 8x8, onde as casas claras são ignoradas e as escuras são numeradas de 1 a 32
// As jogadas são feitas informando a casa de origem e a casa de destino, separadas por um hífen, por exemplo: 9-14
// O programa verifica se a jogada é válida e se há capturas obrigatórias
// O jogo termina quando um dos jogadores não tem mais peças ou não pode se mover

    // Função para criar o tabuleiro inicial
    fun criarTabuleiro(): Array<CharArray> {
        val tabuleiro = Array(8) { CharArray(8) }
        for (i in 0..7) {
            for (j in 0..7) {
                if ((i + j) % 2 == 0) {
                    tabuleiro[i][j] = ' ' // casa clara
                } else {
                    when (i) {
                        in 0..2 -> tabuleiro[i][j] = 'b' // peça branca
                        in 5..7 -> tabuleiro[i][j] = 'p' // peça preta
                        else -> tabuleiro[i][j] = '.' // casa vazia
                    }
                }
            }
        }
        return tabuleiro
    }

    // Função para mostrar o tabuleiro na tela
    fun mostrarTabuleiro(tabuleiro: Array<CharArray>) {
        println("  a b c d e f g h")
        for (i in 0..7) {
            print("${8 - i} ")
            for (j in 0..7) {
                print("${tabuleiro[i][j]} ")
            }
            println("${8 - i}")
        }
        println("  a b c d e f g h")
    }

    // Função para converter uma casa em coordenadas da matriz
    fun casaParaCoordenadas(casa: Int): Pair<Int, Int> {
        val i = 8 - (casa - 1) / 4 - 1
        val j = if (i % 2 == 0) (casa - 1) % 4 * 2 + 1 else (casa - 1) % 4 * 2
        return Pair(i, j)
    }

    // Função para converter coordenadas da matriz em uma casa
    fun coordenadasParaCasa(i: Int, j: Int): Int {
        return (8 - i - 1) * 4 + if (i % 2 == 0) j / 2 + 1 else j / 2
    }

    // Função para verificar se uma casa é válida
    fun casaValida(casa: Int): Boolean {
        return casa in 1..32
    }

    // Função para verificar se uma peça é do jogador
    fun pecaDoJogador(peca: Char, jogador: Char): Boolean {
        return when (jogador) {
            'b' -> peca == 'b' || peca == 'B'
            'p' -> peca == 'p' || peca == 'P'
            else -> false
        }
    }

    // Função para verificar se uma peça é uma dama
    fun pecaDama(peca: Char): Boolean {
        return peca == 'B' || peca == 'P'
    }

    // Função para verificar se uma peça é oposta a outra
    fun pecaOposta(peca1: Char, peca2: Char): Boolean {
        return (peca1 == 'b' || peca1 == 'B') && (peca2 == 'p' || peca2 == 'P') ||
                (peca1 == 'p' || peca1 == 'P') && (peca2 == 'b' || peca2 == 'B')
    }

    // Função para obter uma lista de casas que uma peça pode se mover, sem considerar capturas
    fun obterMovimentos(tabuleiro: Array<CharArray>, casa: Int): List<Int> {
        val movimentos = mutableListOf<Int>()
        val (i, j) = casaParaCoordenadas(casa)
        val peca = tabuleiro[i][j]
        if (peca == '.' || peca == ' ') return movimentos
        if (peca == 'b' || pecaDama(peca)) {
            // Movimento para frente e para a esquerda
            if (i > 0 && j > 0 && tabuleiro[i - 1][j - 1] == '.') {
                movimentos.add(coordenadasParaCasa(i - 1, j - 1))
            }
            // Movimento para frente e para a direita
            if (i > 0 && j < 7 && tabuleiro[i - 1][j + 1] == '.') {
                movimentos.add(coordenadasParaCasa(i - 1, j + 1))
            }
        }
        if (peca == 'p' || pecaDama(peca)) {
            // Movimento para trás e para a esquerda
            if (i < 7 && j > 0 && tabuleiro[i + 1][j - 1] == '.') {
                movimentos.add(coordenadasParaCasa(i + 1, j - 1))
            }
            // Movimento para trás e para a direita
            if (i < 7 && j < 7 && tabuleiro[i + 1][j + 1] == '.') {
                movimentos.add(coordenadasParaCasa(i + 1, j + 1))
            }
        }
        return movimentos
    }

    // Função para obter uma lista de casas que uma peça pode capturar
    fun obterCapturas(tabuleiro: Array<CharArray>, casa: Int): List<Int> {
        val capturas = mutableListOf<Int>()
        val (i, j) = casaParaCoordenadas(casa)
        val peca = tabuleiro[i][j]
        if (peca == '.' || peca == ' ') return capturas
        if (peca == 'b' || pecaDama(peca)) {
            // Captura para frente e para a esquerda
            if (i > 1 && j > 1 && pecaOposta(peca, tabuleiro[i - 1][j - 1]) && tabuleiro[i - 2][j - 2] == '.') {
                capturas.add(coordenadasParaCasa(i - 2, j - 2))
            }
            // Captura para frente e para a direita
            if (i > 1 && j < 6 && pecaOposta(peca, tabuleiro[i - 1][j + 1]) && tabuleiro[i - 2][j + 2] == '.') {
                capturas.add(coordenadasParaCasa(i - 2, j + 2))
            }
        }
        if (peca == 'p' || pecaDama(peca)) {
            // Captura para trás e para a esquerda
            if (i < 6 && j > 1 && pecaOposta(peca, tabuleiro[i + 1][j - 1]) && tabuleiro[i + 2][j - 2] == '.') {
                capturas.add(coordenadasParaCasa(i + 2, j - 2))
            }
            // Captura para trás e para a direita
            if (i < 6 && j < 6 && pecaOposta(peca, tabuleiro[i + 1][j + 1]) && tabuleiro[i + 2][j + 2] == '.') {
                capturas.add(coordenadasParaCasa(i + 2, j + 2))
            }
        }
        return capturas
    }

    // Função para verificar se uma jogada é válida
    fun jogadaValida(tabuleiro: Array<CharArray>, origem: Int, destino: Int, jogador: Char): Boolean {
        if (!casaValida(origem) || !casaValida(destino)) return false
        if (!pecaDoJogador(tabuleiro[casaParaCoordenadas(origem).first][casaParaCoordenadas(origem).second], jogador)) return false
        val capturas = obterCapturas(tabuleiro, origem)
        val movimentos = obterMovimentos(tabuleiro, origem)
        return if (capturas.isNotEmpty()) {
            destino in capturas
        } else {
            destino in movimentos
        }
    }

    // Função para mover uma peça de uma casa para outra, retornando true se houve captura
    fun moverPeca(tabuleiro: Array<CharArray>, origem: Int,

}