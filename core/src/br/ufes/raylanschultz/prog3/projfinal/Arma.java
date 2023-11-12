package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Arma {
    private final float cooldown;
    private float cooldownAtual;
    private final int dano;
    private final int velocidadeProjetil;
    private final Vector2 posicaoRelativa;
    private final Vector2 posicaoRelativaEngine;
    private final Vector2 posicaoRelativaSuporte;
    private final Sprite[] projetilSprites;
    private final Vector2 projetilHitbox;
    private final Sound som;
    private final Sprite[] armaSprites;
    private final Sprite armaEngineSprite;
    private final Sprite suporteSprite;
    private final float somVolume;
    private Music somAlt;

    public Arma(float cooldown, int dano, int velocidadeProjetil, Vector2 posicaoRelativa, Sprite[] projetilSprites, Vector2 projetilHitbox, Sound som) {
        this.cooldown = cooldown;
        this.cooldownAtual = MathUtils.random(cooldown);
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

    public Arma(float cooldown, int dano, int velocidadeProjetil, Vector2 posicaoRelativa, Sprite[] projetilSprites, Vector2 projetilHitbox, Sound som, Sprite[] armaSprites, Sprite armaEngineSprite, Vector2 posicaoRelativaEngine, Sprite suporteSprite, Vector2 posicaoRelativaSuporte, float somVolume) {
        this.cooldown = cooldown;
        this.cooldownAtual = MathUtils.random(cooldown);
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

    public Arma(float cooldown, int dano, int velocidadeProjetil, Vector2 posicaoRelativa, Sprite[] projetilSprites, Vector2 projetilHitbox, Music som, Sprite[] armaSprites, Sprite armaEngineSprite, Vector2 posicaoRelativaEngine, Sprite suporteSprite, Vector2 posicaoRelativaSuporte, float somVolume) {
        this.cooldown = cooldown;
        this.cooldownAtual = MathUtils.random(cooldown);
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

    public Projetil atirar(Nave nave) {
        if (cooldownAtual > 0) {
            return null;
        }
        if (som != null) som.play(somVolume);
        else if (somAlt != null && !somAlt.isPlaying()) somAlt.play();
        cooldownAtual += cooldown;
        final var position = nave.getPosicao().cpy().add(nave.getColisao().x / 2 - projetilHitbox.x / 2 , nave.getColisao().y / 2 - projetilHitbox.x / 2).add(posicaoRelativa.cpy().rotateDeg(nave.getRotacao()));
        final var direcao = new Vector2(0, 1).rotateDeg(nave.getRotacao());
        return new Projetil(projetilSprites, position, direcao, velocidadeProjetil, projetilHitbox, dano, 1, nave);
    }

    public void atualizarFisica(float deltaTime) {
        if (cooldownAtual > 0) {
            cooldownAtual -= deltaTime;
        }
    }

    public Sprite getArmaSprite() {
        return armaSprites != null ? armaSprites[0] : null;
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
