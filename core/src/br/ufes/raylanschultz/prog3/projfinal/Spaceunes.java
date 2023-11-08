package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Spaceunes extends ApplicationAdapter {
    private int atualizacoesPorSegundo = 60;
    private BitmapFont fontePadrao;

    private SpriteBatch gameBatch;
    private OrthographicCamera cameraDoJogo;
    private Viewport gameViewport;

    private ShapeRenderer shapeRenderer;

    private float tempoDesdeUltimaAtualizacao = 0;

    private Array<Entidade> entidadesAliadas = new Array<>();
    private Array<Entidade> projeteisAliados = new Array<>();
    private Array<Entidade> entidadesInimigas = new Array<>();
    private Array<Entidade> projeteisInimigos = new Array<>();

    private Jogador jogador;

    private final static int LARGURA_JOGO = 512;
    private final static int ALTURA_JOGO = 448;

    private boolean renderizarCaixaDeColisao = false;

    private final Array<InterfaceGrafica> interfaceGraficasMenu = new Array<>();
    private InterfaceGrafica interfaceGraficaPausado;
    private InterfaceGrafica interfaceGraficaGameOver;
    private InterfaceGrafica interfaceGraficaPressioneParaContinuar;
    private InterfaceGrafica interfaceGraficaFundo;
    private final Array<Botao> botoes = new Array<>();

    private Cena cenaAtual = Cena.MENU;

    private boolean pausado = false;

    private int rodada = 0;
    private float parteAtual = 0;

    @Override
    public void create() {
        gameBatch = new SpriteBatch();
        cameraDoJogo = new OrthographicCamera();
        cameraDoJogo.setToOrtho(false, LARGURA_JOGO, ALTURA_JOGO);
        gameViewport = new ExtendViewport(LARGURA_JOGO, ALTURA_JOGO, cameraDoJogo);
        final var generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/game_over.ttf"));
        final var parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 64;

        fontePadrao = generator.generateFont(parameter);
        Gdx.input.setInputProcessor(new InputAdapter() {
           @Override
           public boolean touchUp(int screenX, int screenY, int pointer, int button) {
               if (cenaAtual == Cena.MENU) {
                   if (button == Input.Buttons.LEFT) {
                       Vector2 posicaoMouse = gameViewport.unproject(new Vector2(screenX, screenY));
                       for (Botao botao : botoes) {
                            if (botao.clicado(posicaoMouse)) {
                                cenaAtual = Cena.EM_JOGO;
                                novoJogo();
                                return true;
                            }
                       }
                   }
               }
               return false;
           }
        });

        var imagemPausado = new Sprite(new Texture("sprites/ui/paused.png"));
        var imagemGameOver = new Sprite(new Texture("sprites/ui/game-over.png"));
        var imagemPressioneParaContinuar = new Sprite(new Texture("sprites/ui/press-to-continue.png"));

        var fundoVoidJunto = new Texture("sprites/backgrounds/void.png");

        var partes = 9;
        Sprite[] fundoVoids = new Sprite[partes];
        for (int i = 0; i < partes; i++) {
            fundoVoids[i] = new Sprite(fundoVoidJunto, i * (fundoVoidJunto.getWidth() / partes), 0, fundoVoidJunto.getWidth() / partes, fundoVoidJunto.getHeight());
        }

        var fundoEstrelasJunto = new Texture("sprites/backgrounds/stars.png");

        Sprite[] fundoEstrelas = new Sprite[partes];
        for (int i = 0; i < partes; i++) {
            fundoEstrelas[i] = new Sprite(fundoEstrelasJunto, i * (fundoEstrelasJunto.getWidth() / partes), 0, fundoEstrelasJunto.getWidth() / partes, fundoEstrelasJunto.getHeight());
        }

        var fundoEstrelasDistantesJuntas = new Texture("sprites/backgrounds/stars-distant.png");

        Sprite[] fundoEstrelasDistantes = new Sprite[partes];
        for (int i = 0; i < partes; i++) {
            fundoEstrelasDistantes[i] = new Sprite(fundoEstrelasDistantesJuntas, i * (fundoEstrelasDistantesJuntas.getWidth() / partes), 0, fundoEstrelasDistantesJuntas.getWidth() / partes, fundoEstrelasDistantesJuntas.getHeight());
        }

        interfaceGraficaPausado = new InterfaceGrafica() {
            @Override
            public void renderizar(Batch gameBatch, Vector2 posicaoMouse, boolean pressionado) {
                gameBatch.draw(imagemPausado, LARGURA_JOGO / 2.0f - imagemPausado.getWidth() / 2, ALTURA_JOGO / 2.0f - imagemPausado.getHeight() / 2);
            }
        };

        interfaceGraficaGameOver = new InterfaceGrafica() {
            @Override
            public void renderizar(Batch gameBatch, Vector2 posicaoMouse, boolean pressionado) {
                gameBatch.draw(imagemGameOver, LARGURA_JOGO / 2.0f - imagemGameOver.getWidth() / 2, ALTURA_JOGO / 2.0f - imagemGameOver.getHeight() / 2);
            }
        };

        interfaceGraficaPressioneParaContinuar = new InterfaceGrafica() {
            @Override
            public void renderizar(Batch gameBatch, Vector2 posicaoMouse, boolean pressionado) {
                gameBatch.draw(imagemPressioneParaContinuar, LARGURA_JOGO / 2.0f - (imagemPressioneParaContinuar.getWidth() * 0.9f) / 2, ALTURA_JOGO / 2.0f - (imagemPressioneParaContinuar.getHeight() * 0.9f) / 2 - 50, imagemPressioneParaContinuar.getWidth() * 0.9f, imagemPressioneParaContinuar.getHeight() * 0.9f);
            }
        };

        interfaceGraficaFundo = new InterfaceGrafica() {
            @Override
            public void renderizar(Batch gameBatch, Vector2 posicaoMouse, boolean pressionado) {
                if (parteAtual >= partes) parteAtual -= partes;
                for (int x = -2; x <= 2; x++) {
                    for (int y = 0; y <= 1; y++) {
                        gameBatch.draw(fundoVoids[(int) parteAtual], x * fundoVoids[(int) parteAtual].getWidth(), y * fundoVoids[(int) parteAtual].getHeight(), fundoVoids[(int) parteAtual].getWidth(), fundoVoids[(int) parteAtual].getHeight());
                        gameBatch.draw(fundoEstrelas[(int) parteAtual], x * fundoEstrelas[(int) parteAtual].getWidth(), y * fundoEstrelas[(int) parteAtual].getHeight(), fundoEstrelas[(int) parteAtual].getWidth(), fundoEstrelas[(int) parteAtual].getHeight());
                        gameBatch.draw(fundoEstrelasDistantes[(int) parteAtual], x * fundoEstrelasDistantes[(int) parteAtual].getWidth(), y * fundoEstrelasDistantes[(int) parteAtual].getHeight(), fundoEstrelasDistantes[(int) parteAtual].getWidth(), fundoEstrelasDistantes[(int) parteAtual].getHeight());
                    }
                }
//                gameBatch.draw(fundoVoids[(int) parteAtual], 0, 0, fundoVoids[(int) parteAtual].getWidth(), fundoVoids[(int) parteAtual].getHeight());
//                gameBatch.draw(fundoEstrelas[(int) parteAtual], 0, 0, fundoEstrelas[(int) parteAtual].getWidth(), fundoEstrelas[(int) parteAtual].getHeight());
//                gameBatch.draw(fundoEstrelasDistantes[(int) parteAtual], 0, 0, fundoEstrelasDistantes[(int) parteAtual].getWidth(), fundoEstrelasDistantes[(int) parteAtual].getHeight());
            }
        };


        var botaoIniciarTamanho = new Vector2(12*5, 13*5);
        var botaoInicar = new Botao(new Rectangle(LARGURA_JOGO / 2.0f - botaoIniciarTamanho.x / 2, 75, botaoIniciarTamanho.x, botaoIniciarTamanho.y), new Sprite(new Texture("sprites/ui/8x8.png"), 125, 0, 12, 13), new Sprite(new Texture("sprites/ui/8x8.png"), 137, 0, 12, 13), new Sprite(new Texture("sprites/ui/8x8.png"), 149, 0, 12, 13));

        var imagemFundo = new Sprite(new Texture("sprites/ui/background.png"));

        interfaceGraficasMenu.add(new InterfaceGrafica() {
            @Override
            public void renderizar(Batch gameBatch, Vector2 posicaoMouse, boolean pressionado) {
                gameBatch.draw(imagemFundo, -512*2, 0, 512, 512);
                gameBatch.draw(imagemFundo, -512, 0, 512, 512);
                gameBatch.draw(imagemFundo, 0, 0, 512, 512);
                gameBatch.draw(imagemFundo, 512, 0, 512, 512);
                gameBatch.draw(imagemFundo, 512*2, 0, 512, 512);
            }
        });

        var imagemTitulo = new Sprite(new Texture("sprites/ui/title.png"));
        interfaceGraficasMenu.add(new InterfaceGrafica() {
            @Override
            public void renderizar(Batch gameBatch, Vector2 posicaoMouse, boolean pressionado) {
                gameBatch.draw(imagemTitulo, LARGURA_JOGO / 2.0f - imagemTitulo.getWidth() / 2, ALTURA_JOGO - 150);
            }
        });

        interfaceGraficasMenu.add(botaoInicar);
        botoes.add(botaoInicar);
        shapeRenderer = new ShapeRenderer();
    }

    private void novoJogo() {
        rodada = 0;
        entidadesAliadas.clear();
        projeteisAliados.clear();
        entidadesInimigas.clear();
        projeteisInimigos.clear();

        var jogadorSprites = new Sprite[] {
                new Sprite(new Texture("sprites/ship/base/full.png")),
                new Sprite(new Texture("sprites/ship/base/damage1.png")),
                new Sprite(new Texture("sprites/ship/base/damage2.png")),
                new Sprite(new Texture("sprites/ship/base/damage3.png"))
        };

        jogador = new Jogador(jogadorSprites, new Vector2(0, 0), new Vector2(20,20), 100);
        entidadesAliadas.add(jogador);
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }

    public void tickUpdateEntidade(Entidade entidade, Array<Entidade> entidades, int index) {
        entidade.atualizarFisica(1.0f / atualizacoesPorSegundo);
        if (entidade.estaDestruido()) {
            entidades.removeIndex(index);
        } else if (!(entidade instanceof Projetil)) {
            if (entidade.getPosicao().y + entidade.getColisao().y > ALTURA_JOGO) {
                entidade.setPosicao(new Vector2(entidade.getPosicao().x, ALTURA_JOGO - entidade.getColisao().y));
            } else if (entidade.getPosicao().y < 0) {
                entidade.setPosicao(new Vector2(entidade.getPosicao().x, 0));
            }
            if (entidade.getPosicao().x + entidade.getColisao().x > LARGURA_JOGO) {
                entidade.setPosicao(new Vector2(LARGURA_JOGO - entidade.getColisao().x, entidade.getPosicao().y));
            } else if (entidade.getPosicao().x < 0) {
                entidade.setPosicao(new Vector2(0, entidade.getPosicao().y));
            }
        }
    }

    public void tickUpdateEntidadeAliada(Entidade entidade, int index, Vector2 posicaoMouse) {
        ((Nave) entidade).setOlharPara(posicaoMouse);
        this.tickUpdateEntidade(entidade, entidadesAliadas, index);
        entidade.checarColisao(entidadesInimigas);
        entidade.checarColisao(projeteisInimigos);
    }

    public void tickUpdateEntidadeInimiga(Entidade entidade, int index) {
        ((Inimigo) entidade).setOlharPara(jogador.getPosicao().cpy().add(jogador.getColisao().cpy().scl(0.5f)));
        Projetil projetil = ((Inimigo) entidade).atirar(jogador);
        if (projetil != null) projeteisInimigos.add(projetil);
        this.tickUpdateEntidade(entidade, entidadesInimigas, index);
        entidade.checarColisao(entidadesAliadas);
        entidade.checarColisao(projeteisAliados);
    }

    public void tickUpdateProjetil(Entidade entidade, Array<Entidade> projeteis, int index) {
        if (entidade.getPosicao().y > ALTURA_JOGO) {
            projeteis.removeIndex(index);
            return;
        } else if (entidade.getPosicao().y < 0) {
            projeteis.removeIndex(index);
            return;
        } else if (entidade.getPosicao().x > LARGURA_JOGO) {
            projeteis.removeIndex(index);
            return;
        } else if (entidade.getPosicao().x < 0) {
            projeteis.removeIndex(index);
            return;
        }
        this.tickUpdateEntidade(entidade, projeteis, index);
    }

    public void tickUpdateProjetilAliado(Entidade entidade, int index) {
        this.tickUpdateProjetil(entidade, projeteisAliados, index);
        entidade.checarColisao(entidadesInimigas);
    }

    public void tickUpdateProjetilInimigo(Entidade entidade, int index) {
        this.tickUpdateProjetil(entidade, projeteisInimigos, index);
        entidade.checarColisao(entidadesAliadas);
    }

    public void tickUpdate(Vector2 posicaoMouse) {
        for (int i = 0; i < entidadesAliadas.size; i++) {
            tickUpdateEntidadeAliada(entidadesAliadas.get(i), i, posicaoMouse);
        }
        for (int i = 0; i < entidadesInimigas.size; i++) {
            tickUpdateEntidadeInimiga(entidadesInimigas.get(i), i);
        }
        for (int i = 0; i < projeteisAliados.size; i++) {
            tickUpdateProjetilAliado(projeteisAliados.get(i), i);
        }
        for (int i = 0; i < projeteisInimigos.size; i++) {
            tickUpdateProjetilInimigo(projeteisInimigos.get(i), i);
        }
    }

    public void renderizarEntidade(Entidade entidade, float interpolation, Vector2 posicaoMouse) {
        interpolation = pausado ? 1 : interpolation;
        var posicaoRenderizada = entidade.posicaoRenderizada(interpolation);
        if (posicaoRenderizada == null) return;
        entidade.getImagem().setPosition(posicaoRenderizada.x - entidade.getImagem().getWidth() / 2 + entidade.getColisao().x / 2, posicaoRenderizada.y - entidade.getImagem().getHeight() / 2 + entidade.getColisao().y / 2);
        Sprite armaSprite = null;
        Sprite armaEngineSprite = null;
        if (entidade instanceof Nave) {
            entidade.getImagem().setRotation(((Nave) entidade).getRotacaoRenderizada(interpolation));
            final var arma = ((Nave) entidade).getArma();
            if (arma != null) {
                armaSprite = arma.getArmaSprite();
                armaEngineSprite = arma.getArmaEngineSprite();
            }
            if (armaSprite != null) {
                armaSprite.setPosition(posicaoRenderizada.x + arma.getPosicaoRelativa().x - entidade.getImagem().getWidth() / 2 + entidade.getColisao().x / 2, posicaoRenderizada.y + arma.getPosicaoRelativa().y - entidade.getImagem().getHeight() / 2 + entidade.getColisao().y / 2);
                armaSprite.setRotation(((Nave) entidade).getRotacaoRenderizada(interpolation));
                armaSprite.draw(gameBatch);

                armaEngineSprite.setPosition(posicaoRenderizada.x - arma.getPosicaoRelativaEngine().x - entidade.getImagem().getWidth() / 2 + entidade.getColisao().x / 2, posicaoRenderizada.y - arma.getPosicaoRelativaEngine().y - entidade.getImagem().getHeight() / 2 + entidade.getColisao().y / 2);
                armaEngineSprite.setRotation(((Nave) entidade).getRotacaoRenderizada(interpolation));
                armaEngineSprite.draw(gameBatch);
            }
        } else if (entidade instanceof Projetil) {
            entidade.getImagem().setRotation(entidade.getVelocidade().angleDeg() - 90);
        }
        entidade.getImagem().draw(gameBatch);
        if (renderizarCaixaDeColisao)
            shapeRenderer.rect(entidade.getPosicao().x, entidade.getPosicao().y, entidade.getColisao().x, entidade.getColisao().y);
    }

    @Override
    public void render() {
        Vector2 posicaoMouse = gameViewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        ScreenUtils.clear(0, 0, 0, 1);

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            atualizacoesPorSegundo = atualizacoesPorSegundo == 60 ? 7 : atualizacoesPorSegundo == 7 ? 10000 : 60;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            renderizarCaixaDeColisao = !renderizarCaixaDeColisao;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            switch (cenaAtual) {
                case MENU:
                    Gdx.app.exit();
                    break;
                case EM_JOGO:
                    if (!jogador.estaDestruido()) {
                        pausado = !pausado;
                    } else {
                        cenaAtual = Cena.MENU;
                    }
                    break;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (cenaAtual == Cena.EM_JOGO) {
                if (jogador.estaDestruido()) {
                    novoJogo();
                }
            }
        }

        cameraDoJogo.update();
        gameBatch.setProjectionMatrix(cameraDoJogo.combined);
        gameBatch.begin();

        switch (cenaAtual) {
            case MENU:
                for (InterfaceGrafica interfaceGrafica : interfaceGraficasMenu) {
                    interfaceGrafica.renderizar(gameBatch, posicaoMouse, Gdx.input.isButtonPressed(Input.Buttons.LEFT));
                }
                break;
            case EM_JOGO:
                float deltaTime = Gdx.graphics.getDeltaTime();

                tempoDesdeUltimaAtualizacao += deltaTime;

                while (tempoDesdeUltimaAtualizacao >= 1f / atualizacoesPorSegundo) {
                    if (!pausado) tickUpdate(posicaoMouse);
                    tempoDesdeUltimaAtualizacao -= 1f / atualizacoesPorSegundo;
                }

                if (!pausado) parteAtual += deltaTime * 10f;
                interfaceGraficaFundo.renderizar(gameBatch, posicaoMouse, Gdx.input.isButtonPressed(Input.Buttons.LEFT));

                if (entidadesInimigas.size == 0) {
                    rodada++;
                    for (int i = 0; i < (rodada == 1 ? 1 : (rodada-1)*2); i++) {
                        entidadesInimigas.add(new Inimigo(new Sprite(new Texture("sprites/enemies/base/fighter.png")), new Vector2(MathUtils.random(LARGURA_JOGO), ALTURA_JOGO - 96), new Vector2(24,24), CriadorArmas.basica(), 25, 100f));
                    }
                }

                var interpolation = tempoDesdeUltimaAtualizacao * atualizacoesPorSegundo;

                shapeRenderer.setProjectionMatrix(cameraDoJogo.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

                for (Entidade entidade : entidadesAliadas) {
                    if (!pausado) entidade.atualizarQuadro(deltaTime);
                    renderizarEntidade(entidade, interpolation, posicaoMouse);
                    if (entidade instanceof Jogador && !pausado) {
                        var xInput = Gdx.input.isKeyPressed(Input.Keys.D) ? 1 : Gdx.input.isKeyPressed(Input.Keys.A) ? -1 : 0;
                        var yInput = Gdx.input.isKeyPressed(Input.Keys.W) ? 1 : Gdx.input.isKeyPressed(Input.Keys.S) ? -1 : 0;
                        entidade.setMovimento(new Vector2(xInput, yInput));
                        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                            Projetil projetil = ((Jogador) entidade).atirar(posicaoMouse);
                            if (projetil != null) projeteisAliados.add(projetil);
                        }
                    }
                }
                for (Entidade entidade : entidadesInimigas) {
                    if (!pausado) entidade.atualizarQuadro(deltaTime);
                    renderizarEntidade(entidade, interpolation, posicaoMouse);
                }
                for (Entidade entidade : projeteisAliados) {
                    if (!pausado) entidade.atualizarQuadro(deltaTime);
                    renderizarEntidade(entidade, interpolation, posicaoMouse);
                }
                for (Entidade entidade : projeteisInimigos) {
                    if (!pausado) entidade.atualizarQuadro(deltaTime);
                    renderizarEntidade(entidade, interpolation, posicaoMouse);
                }

                fontePadrao.draw(gameBatch, "Quadros por segundo: " + Gdx.graphics.getFramesPerSecond(), 0, 20);
                fontePadrao.draw(gameBatch, "Atualizações por segundo: " + atualizacoesPorSegundo, 0, 40);
                fontePadrao.draw(gameBatch, "Vida: " + jogador.getVida(), 0, ALTURA_JOGO - 20);
                fontePadrao.draw(gameBatch, "XP: " + jogador.getXp(), 0, ALTURA_JOGO - 60);
                if (pausado) {
                    interfaceGraficaPausado.renderizar(gameBatch, posicaoMouse, Gdx.input.isButtonPressed(Input.Buttons.LEFT));
                }

                if (jogador.estaDestruido()) {
                    interfaceGraficaGameOver.renderizar(gameBatch, posicaoMouse, Gdx.input.isButtonPressed(Input.Buttons.LEFT));
                    interfaceGraficaPressioneParaContinuar.renderizar(gameBatch, posicaoMouse, Gdx.input.isButtonPressed(Input.Buttons.LEFT));
                } else {
                    if (jogador.querendoTrocarArma()) {
                        fontePadrao.draw(gameBatch, "Escolha a arma: ", LARGURA_JOGO / 2.0f - 100, ALTURA_JOGO / 2.0f + 20);

                    }
                }

                break;
        }

        shapeRenderer.end();
        gameBatch.end();
    }

    @Override
    public void dispose() {
        gameBatch.dispose();
        fontePadrao.dispose();
    }
}
