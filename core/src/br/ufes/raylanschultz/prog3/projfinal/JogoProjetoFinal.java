package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class JogoProjetoFinal extends ApplicationAdapter {
    private final int TICKS = 60;
    private BitmapFont defaultFont;

    private SpriteBatch gameBatch;
    private OrthographicCamera gameCamera;
    private Viewport gameViewport;

    private ShapeRenderer shapeRenderer;

    private float timeAfterLastTick = 0;

    private Array<Entidade> entidadesAliadas = new Array<>();
    private Array<Entidade> projeteisAliados = new Array<>();
    private Array<Entidade> entidadesInimigas = new Array<>();
    private Array<Entidade> projeteisInimigos = new Array<>();

    private Jogador jogador;

    private final static int GAME_WIDTH = 512;
    private final static int GAME_HEIGHT = 448;

    @Override
    public void create() {
        gameBatch = new SpriteBatch();
        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
        gameViewport = new ExtendViewport(GAME_WIDTH, GAME_HEIGHT, gameCamera);
        defaultFont = new BitmapFont();

        var jogadorSprites = new Sprite[] {
                new Sprite(new Texture("sprites/ship/base/full.png")),
                new Sprite(new Texture("sprites/ship/base/damage1.png")),
                new Sprite(new Texture("sprites/ship/base/damage2.png")),
                new Sprite(new Texture("sprites/ship/base/damage3.png"))
        };

        jogador = new Jogador(jogadorSprites, new Vector2(0, 0), new Vector2(20,20), 100);

        entidadesAliadas.add(jogador);

        final var projetilTexture = new Texture("sprites/enemies/projectiles/bullet.png");

        for (int i = 0; i < 1; i++) {
            final var projetilSprites = new Sprite[]{new Sprite(projetilTexture, 0, 0, 4, 16), new Sprite(projetilTexture, 4, 0, 4, 16), new Sprite(projetilTexture, 8, 0, 4, 16), new Sprite(projetilTexture, 12, 0, 4, 16)};

            entidadesInimigas.add(new Inimigo(new Sprite(new Texture("sprites/enemies/base/fighter.png")), new Vector2(MathUtils.random(GAME_WIDTH), GAME_HEIGHT - 96), new Vector2(24,24), new Arma(0.5f, 5, 100, new Vector2(0, 0), projetilSprites, new Vector2(8, 8)), 25));
        }
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }

    public void tickUpdateEntidadeAliada(Entidade entidade) {
        if (entidade instanceof Jogador)  {
            var xInput = Gdx.input.isKeyPressed(Input.Keys.RIGHT) ? 1 : Gdx.input.isKeyPressed(Input.Keys.LEFT) ? -1 : 0;
            var yInput = Gdx.input.isKeyPressed(Input.Keys.UP) ? 1 : Gdx.input.isKeyPressed(Input.Keys.DOWN) ? -1 : 0;
            entidade.setMovimento(new Vector2(xInput, yInput));
            if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
                Projetil projetil = ((Jogador) entidade).atirar();
                if (projetil != null) projeteisAliados.add(projetil);
            }
            entidade.atualizacaoDeFrame(1.0f / TICKS);
            entidade.checarColisao(entidadesInimigas);
            entidade.checarColisao(projeteisInimigos);
        }
    }

    public void tickUpdateEntidadeInimiga(Entidade entidade) {
        if (entidade instanceof Inimigo) {
            Projetil projetil = ((Inimigo) entidade).atirar(jogador);
            if (projetil != null) projeteisInimigos.add(projetil);
        }
        entidade.atualizacaoDeFrame(1.0f / TICKS);
        entidade.checarColisao(entidadesAliadas);
        entidade.checarColisao(projeteisAliados);
    }

    public void tickUpdateProjetil(Entidade entidade, int i, Array<Entidade> projeteis) {
        if (entidade instanceof Projetil) {
            if (entidade.getPosicao().y > GAME_HEIGHT) {
                projeteis.removeIndex(i);
            } else if (entidade.getPosicao().y < 0) {
                projeteis.removeIndex(i);
            } else if (entidade.getPosicao().x > GAME_WIDTH) {
                projeteis.removeIndex(i);
            } else if (entidade.getPosicao().x < 0) {
                projeteis.removeIndex(i);
            }
        }
    }

    public void tickUpdateProjetilAliado(Entidade entidade, int i) {
        this.tickUpdateProjetil(entidade, i, projeteisAliados);
        entidade.atualizacaoDeFrame(1.0f / TICKS);
        entidade.checarColisao(entidadesInimigas);
    }

    public void tickUpdateProjetilInimigo(Entidade entidade, int i) {
        this.tickUpdateProjetil(entidade, i, projeteisInimigos);
        entidade.atualizacaoDeFrame(1.0f / TICKS);
        entidade.checarColisao(entidadesAliadas);
    }

    public void tickUpdate() {
        for (int i = 0; i < entidadesAliadas.size; i++) {
            tickUpdateEntidadeAliada(entidadesAliadas.get(i));
        }
        for (int i = 0; i < entidadesInimigas.size; i++) {
            tickUpdateEntidadeInimiga(entidadesInimigas.get(i));
        }
        for (int i = 0; i < projeteisAliados.size; i++) {
            tickUpdateProjetilAliado(projeteisAliados.get(i), i);
        }
        for (int i = 0; i < projeteisInimigos.size; i++) {
            tickUpdateProjetilInimigo(projeteisInimigos.get(i), i);
        }
    }

    public void renderEntidade(Entidade entidade, float interpolation) {
        Vector2 interpolatedPosition = entidade.posicaoRenderizada(interpolation);
        entidade.getImagem().setPosition(interpolatedPosition.x - entidade.getImagem().getWidth() / 2 + entidade.getColisao().x / 2, interpolatedPosition.y - entidade.getImagem().getHeight() / 2 + entidade.getColisao().y / 2);

        if (entidade instanceof Jogador) {
            defaultFont.draw(gameBatch, "Vida: " + ((Jogador) entidade).getVida(), 0, 120);
        }

        if (entidade instanceof Inimigo) {
            final var centroJogador = jogador.getPosicao().cpy().add(jogador.getColisao().cpy().scl(0.5f));
            final var centroInimigo = entidade.getPosicao().cpy().add(entidade.getColisao().cpy().scl(0.5f));

            final var degree = centroJogador.cpy().sub(centroInimigo).angleDeg() - 90;
            entidade.getImagem().setRotation(degree);
        }
        if (entidade instanceof Projetil) {
            entidade.getImagem().setRotation(entidade.getVelocidade().angleDeg() - 90);
        }

        entidade.getImagem().draw(gameBatch);
        // draw hitbox
            shapeRenderer.rect(entidade.getPosicao().x, entidade.getPosicao().y, entidade.getColisao().x, entidade.getColisao().y);
    }

    @Override
    public void render() {
        timeAfterLastTick += Gdx.graphics.getDeltaTime();

        while (timeAfterLastTick >= 1f / TICKS) {
            tickUpdate();
            timeAfterLastTick -= 1f / TICKS;
        }
        ScreenUtils.clear(0, 0, 0, 1);
        gameCamera.update();
        gameBatch.setProjectionMatrix(gameCamera.combined);
        gameBatch.begin();
        defaultFont.draw(gameBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 0, 20);

        var interpolation = timeAfterLastTick * TICKS;

        shapeRenderer.setProjectionMatrix(gameCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (Entidade entidade : entidadesAliadas) {
            renderEntidade(entidade, interpolation);
        }
        for (Entidade entidade : entidadesInimigas) {
            renderEntidade(entidade, interpolation);
        }
        for (Entidade entidade : projeteisAliados) {
            renderEntidade(entidade, interpolation);
        }
        for (Entidade entidade : projeteisInimigos) {
            renderEntidade(entidade, interpolation);
        }
        shapeRenderer.end();
        gameBatch.end();
    }

    @Override
    public void dispose() {
        gameBatch.dispose();
        defaultFont.dispose();
    }
}
