package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class BomberInimigo extends InimigoBasico implements Atirador {
    private final Jogador jogador;
    public BomberInimigo(Jogador jogador, Sprite sprite, Vector2 position, Vector2 hitbox, int vida, float velocidadeRotacao, Sprite[] destruicao, float atrito, float aceleracao, float velocidadeMaxima) {
        super(sprite, position, hitbox, null, vida, velocidadeRotacao, destruicao, atrito, aceleracao, velocidadeMaxima);
        this.jogador = jogador;
    }

    @Override
    public void atualizarFisica(float deltaTime) {
        super.atualizarFisica(deltaTime);
        final var alvo = jogador.getPosicao().cpy().add(jogador.getColisao().cpy().scl(0.5f));
        if (jogador.estaDestruido()) this.setMovimento(Vector2.Zero); else this.setMovimento(alvo.cpy().sub(this.getPosicao().cpy().add(this.getColisao().cpy().scl(0.5f))).nor());
    }

    @Override
    public Projetil atirar(Jogador jogador) {
        return null;
    }

    @Override
    public float getColisaoDano() {
        return 0;
    }

    @Override
    public void colidir(Entidade entidade, float deltaTime) {
        super.colidir(entidade, deltaTime);
        if (entidade == jogador && !jogador.estaDestruido()) {
            jogador.aplicarDanoDireto(30);
            this.vida = 0;
            this.frameAtual = 0;
        }
    };
}
