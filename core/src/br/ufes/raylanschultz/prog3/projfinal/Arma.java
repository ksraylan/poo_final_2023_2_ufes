package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Arma {
    protected final float cooldown;
    protected float cooldownAtual = 0;
    protected final int dano;
    protected final int velocidadeProjetil;
    protected final Vector2 posicaoRelativa;
    protected final Vector2 posicaoRelativaEngine;
    protected final Vector2 posicaoRelativaSuporte;
    protected final Sprite[] projetilSprites;
    protected final Vector2 projetilHitbox;
    protected final Sound som;
    protected final Sprite[] armaSprites;
    protected final Sprite armaEngineSprite;
    protected final Sprite suporteSprite;
    protected final float somVolume;
    protected Music somAlt;
    protected Nave nave;
    protected Array<Entidade> projeteis;
    protected int quadroAtual = 0;

    public Arma(Nave nave, Array<Entidade> projeteis, float cooldown, int dano, int velocidadeProjetil, Vector2 posicaoRelativa, Sprite[] projetilSprites, Vector2 projetilHitbox, Sound som) {
        this.nave = nave;
        this.projeteis = projeteis;
        this.cooldown = cooldown;
        this.dano = dano;
        this.velocidadeProjetil = velocidadeProjetil;
        this.posicaoRelativa = posicaoRelativa;
        this.projetilSprites = projetilSprites;
        this.projetilHitbox = projetilHitbox;
        this.som = som;
        this.armaSprites = null;
        this.armaEngineSprite = null;
        this.suporteSprite = null;
        this.posicaoRelativaEngine = null;
        this.posicaoRelativaSuporte = null;
        this.somVolume = 0.25f;
    }

    public Arma(Nave nave, Array<Entidade> projeteis, float cooldown, int dano, int velocidadeProjetil, Vector2 posicaoRelativa, Sprite[] projetilSprites, Vector2 projetilHitbox, Sound som, Sprite[] armaSprites, Sprite armaEngineSprite, Vector2 posicaoRelativaEngine, Sprite suporteSprite, Vector2 posicaoRelativaSuporte, float somVolume) {
        this.nave = nave;
        this.projeteis = projeteis;
        this.cooldown = cooldown;
        this.dano = dano;
        this.velocidadeProjetil = velocidadeProjetil;
        this.posicaoRelativa = posicaoRelativa;
        this.projetilSprites = projetilSprites;
        this.projetilHitbox = projetilHitbox;
        this.som = som;
        this.armaSprites = armaSprites;
        this.armaEngineSprite = armaEngineSprite;
        this.suporteSprite = suporteSprite;
        this.posicaoRelativaEngine = posicaoRelativaEngine;
        this.posicaoRelativaSuporte = posicaoRelativaSuporte;
        this.somVolume = somVolume;
        armaEngineSprite.setOrigin(armaEngineSprite.getWidth() / 2 - posicaoRelativaEngine.x, armaEngineSprite.getHeight() / 2 - posicaoRelativaEngine.y);
        suporteSprite.setOrigin(suporteSprite.getWidth() / 2 - posicaoRelativaSuporte.x, suporteSprite.getHeight() / 2 - posicaoRelativaSuporte.y);
        for (var sprite : armaSprites) {
            sprite.setOrigin(sprite.getWidth() / 2 - posicaoRelativa.x, sprite.getHeight() / 2 - posicaoRelativa.y);
        }
    }

    public Arma(Nave nave, Array<Entidade> projeteis, float cooldown, int dano, int velocidadeProjetil, Vector2 posicaoRelativa, Sprite[] projetilSprites, Vector2 projetilHitbox, Music som, Sprite[] armaSprites, Sprite armaEngineSprite, Vector2 posicaoRelativaEngine, Sprite suporteSprite, Vector2 posicaoRelativaSuporte, float somVolume) {
        this.nave = nave;
        this.projeteis = projeteis;
        this.cooldown = cooldown;
        this.dano = dano;
        this.velocidadeProjetil = velocidadeProjetil;
        this.posicaoRelativa = posicaoRelativa;
        this.projetilSprites = projetilSprites;
        this.projetilHitbox = projetilHitbox;
        this.som = null;
        this.somAlt = som;
        this.somAlt.setVolume(somVolume);
        this.armaSprites = armaSprites;
        this.armaEngineSprite = armaEngineSprite;
        this.suporteSprite = suporteSprite;
        this.posicaoRelativaEngine = posicaoRelativaEngine;
        this.posicaoRelativaSuporte = posicaoRelativaSuporte;
        this.somVolume = somVolume;
        armaEngineSprite.setOrigin(armaEngineSprite.getWidth() / 2 - posicaoRelativaEngine.x, armaEngineSprite.getHeight() / 2 - posicaoRelativaEngine.y);
        suporteSprite.setOrigin(suporteSprite.getWidth() / 2 - posicaoRelativaSuporte.x, suporteSprite.getHeight() / 2 - posicaoRelativaSuporte.y);
        for (var sprite : armaSprites) {
            sprite.setOrigin(sprite.getWidth() / 2 - posicaoRelativa.x, sprite.getHeight() / 2 - posicaoRelativa.y);
        }
    }

    public void atirar() {
        if (cooldownAtual > 0) {
            return;
        }
        if (som != null) som.play(somVolume);
        else if (somAlt != null && !somAlt.isPlaying()) somAlt.play();
        cooldownAtual += cooldown;
        final var position = nave.getPosicao().cpy().add(nave.getColisao().x / 2 - projetilHitbox.x / 2 , nave.getColisao().y / 2 - projetilHitbox.x / 2).add(posicaoRelativa.cpy().rotateDeg(nave.getRotacao()));
        final var direcao = new Vector2(0, 1).rotateDeg(nave.getRotacao());
        projeteis.add(new Projetil(projetilSprites, position, direcao, velocidadeProjetil, projetilHitbox, dano, 1, nave));
    }

    public void liberar() {}

    public void atualizarFisica(float deltaTime) {
        if (cooldownAtual > 0) {
            cooldownAtual -= deltaTime;
        }
    }

    public Sprite getArmaSprite() {
        return armaSprites != null ? armaSprites[quadroAtual] : null;
    }

    public Sprite getArmaEngineSprite() {
        return armaEngineSprite;
    }

    public Sprite getSuporteSprite() {
        return suporteSprite;
    }

    public Vector2 getPosicaoRelativa() {
        return posicaoRelativa;
    }

    public Vector2 getPosicaoRelativaEngine() {
        return posicaoRelativaEngine;
    }

    public Vector2 getPosicaoRelativaSuporte() {
        return posicaoRelativaSuporte;
    }
}
