package br.ufes.raylanschultz.prog3.projfinal;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Jogador extends EntidadeComVida implements Atirador {
    private Array<Arma> armas;

    public Jogador(Sprite[] imagens, Vector2 position, Vector2 hitbox, int vida) {
        super(imagens, position, hitbox, 0.1f, 1000f, 1000f, vida);

        final var projetilSprite = new Sprite(new Texture("sprites/weapons/laser.png"), 0, 16, 16, 16);
        armas = new Array<>() {
            {
                add(new Arma(0.5f, 10, 1000, new Vector2(0, 0), projetilSprite, new Vector2(16, 16)));
            }
        };
    }

    public Projetil atirar() {
        return armas.get(0).atirar(this, Vector2.Y);
    }

    @Override
    public void atualizacaoDeFrame(float deltaTime) {
        super.atualizacaoDeFrame(deltaTime);
        if (this.getVida() > this.getVidaMaxima() / 2) {
            this.setFrameAtual(0);
        } else if (this.getVida() > this.getVidaMaxima() / 4) {
            this.setFrameAtual(1);
        } else if (this.getVida() > 0) {
            this.setFrameAtual(2);
        } else {
            this.setFrameAtual(3);
        }
        armas.forEach(arma -> arma.atualizacaoDeFrame(deltaTime));
    }
}
