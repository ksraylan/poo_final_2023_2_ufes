package br.ufes.raylanschultz.prog3.projfinal;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Jogador extends Nave implements Atirador {
    private int xp = 0;
    private int pontos = 0;
    private int nivel = 1;
    private boolean trocouArma = false;

    public Jogador(Sprite[] imagens, Vector2 position, Vector2 hitbox, int vida) {
        super(imagens, position, hitbox, 5f, 1000f, 1000f, vida, 200f);

        arma = CriadorArmas.gun();
    }

    public Projetil atirar(Vector2 posicaoMouse) {
        return arma.atirar(this);
    }

    @Override
    public void atualizarFisica(float deltaTime) {
        super.atualizarFisica(deltaTime);
        arma.atualizarFisica(deltaTime);
    }

    @Override
    public void atualizarQuadro(float deltaTime) {
        if (this.getVida() > this.getVidaMaxima() / 2) {
            this.setFrameAtual(0);
        } else if (this.getVida() > this.getVidaMaxima() / 4) {
            this.setFrameAtual(1);
        } else if (this.getVida() > 0) {
            this.setFrameAtual(2);
        } else {
            this.setFrameAtual(3);
        }
    }

    public void addXp(int xp) {
        this.xp += xp;
        final var proximoNivel = 20 * (int) Math.pow(2, this.nivel - 1);
        if (this.xp >= proximoNivel) {
            this.xp -= proximoNivel;
            this.nivel++;
            this.pontos++;
        }
    }

    public boolean querendoTrocarArma() {
        return true;
//        return !this.trocouArma && this.pontos > 0;
    }

    public void trocarArma(Arma armaNova) {
        if (this.trocouArma || this.pontos == 0) {
            return;
        }
        this.arma = armaNova;
        this.trocouArma = true;
    }

    public int getPontos() {
        return pontos;
    }

    public int getXp() {
        return xp;
    }
}
