package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Spaceunes extends ApplicationAdapter {
    private final static int LARGURA_JOGO = 854;
    private final static int ALTURA_JOGO = 480;
    private static final int RODADA_CHEFE = 10;
    private static final float INTERVALO_ENTRE_ASTEROIDES = 1.0f;
    private final Array<Entidade> entidadesAliadas = new Array<>();
    private final Array<Entidade> projeteisAliados = new Array<>();
    private final Array<Entidade> entidadesInimigas = new Array<>();
    private final Array<Entidade> projeteisInimigos = new Array<>();
    private final Array<Entidade> asteroides = new Array<>();
    private final Array<InterfaceGrafica> interfaceGraficasMenu = new Array<>();
    private final Array<Botao> botoes = new Array<>();
    private final int escolherArmaSeparar = 96;
    private final int escolherArmaOffset = 259;
    private final int escolherArmaY = 128;
    private int atualizacoesPorSegundo = 60;
    private BitmapFont fontePadrao;
    private SpriteBatch gameBatch;
    private OrthographicCamera cameraDoJogo;
    private Viewport gameViewport;
    private ShapeRenderer shapeRenderer;
    private float tempoDesdeUltimaAtualizacao = 0;
    private NaveAliada naveAliada;
    private boolean renderizarCaixaDeColisao = false;
    private InterfaceGrafica interfaceGraficaPausado;
    private InterfaceGrafica interfaceGraficaGameOver;
    private InterfaceGrafica interfaceGraficaPressioneParaContinuar;
    private InterfaceGrafica interfaceGraficaFundo;
    private InterfaceGrafica interfaceGraficaEscolherArmaFundo;
    private Cena cenaAtual = Cena.MENU;
    private boolean pausado = false;
    private int rodada = 0;
    private float parteAtual = 0;
    private Arma[] armas = null;
    private Sprite[] jogadorSprites = null;
    private Sprite[] jogadorDestruicaoSprites = null;
    private boolean podeTrocarArma = false;
    private boolean podeUpgrade = false;
    private Sprite[] inimigoBasicoDestruicaoSprites = null;
    private Sprite[] bomberInimigoDestruicaoSprites = null;
    private Sprite[] suporteInimigoDestruicaoSprites = null;
    private Sprite inimigoBasicoSprite = null;
    private Sprite bomberInimigoSprite = null;
    private Sprite suporteInimigoSprite = null;
    private boolean trocarArmaDebug = false;
    private boolean stepFrames = false;
    private boolean steppingFrame = false;
    private float intervaloAtualEntreAsteroides = 0f;
    private float timerChefe = 0;
    private int intercalarTiposInimigosChefe = 0;

    private float tempoAtualEntreRodadas = 0f;

    private Music music;

    @Override
    public void create() {
        gameBatch = new SpriteBatch();
        cameraDoJogo = new OrthographicCamera();
        cameraDoJogo.setToOrtho(false, LARGURA_JOGO, ALTURA_JOGO);
        gameViewport = new FitViewport(LARGURA_JOGO, ALTURA_JOGO, cameraDoJogo);
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
                                novoJogo();
                                cenaAtual = Cena.EM_JOGO;
                                return true;
                            }
                        }
                    }
                } else if (cenaAtual == Cena.EM_JOGO) {
                    if (button == Input.Buttons.LEFT) {
                        naveAliada.getArma().liberar();
                    }
                }
                return false;
            }
        });

        var imagemPausado = new Sprite(new Texture("sprites/ui/paused.png"));
        var imagemGameOver = new Sprite(new Texture("sprites/ui/game-over.png"));
        var imagemPressioneParaContinuar = new Sprite(new Texture("sprites/ui/press-to-continue.png"));
        var imagemEscolherArmaFundo = new Sprite(new Texture("sprites/ui/choose-weapon.png"));

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

        inimigoBasicoSprite = new Sprite(new Texture("sprites/enemies/base/fighter.png"));
        bomberInimigoSprite = new Sprite(new Texture("sprites/enemies/base/bomber.png"));
        suporteInimigoSprite = new Sprite(new Texture("sprites/enemies/base/support.png"));

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

        interfaceGraficaEscolherArmaFundo = new InterfaceGrafica() {
            @Override
            public void renderizar(Batch gameBatch, Vector2 posicaoMouse, boolean pressionado) {
                gameBatch.draw(imagemEscolherArmaFundo, LARGURA_JOGO / 2.0f - imagemEscolherArmaFundo.getWidth() / 2, ALTURA_JOGO / 2.0f - imagemEscolherArmaFundo.getHeight() / 2 - 45);
            }
        };

        interfaceGraficaFundo = new InterfaceGrafica() {
            @Override
            public void renderizar(Batch gameBatch, Vector2 posicaoMouse, boolean pressionado) {
                while (parteAtual >= partes) parteAtual -= partes;
                for (int x = 0; x <= 1; x++) {
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


        var botaoIniciarTamanho = new Vector2(12 * 5, 13 * 5);
        var botaoInicar = new Botao(new Rectangle(LARGURA_JOGO / 2.0f - botaoIniciarTamanho.x / 2, 75, botaoIniciarTamanho.x, botaoIniciarTamanho.y), new Sprite(new Texture("sprites/ui/8x8.png"), 125, 0, 12, 13), new Sprite(new Texture("sprites/ui/8x8.png"), 137, 0, 12, 13), new Sprite(new Texture("sprites/ui/8x8.png"), 149, 0, 12, 13));

        var imagemFundo = new Sprite(new Texture("sprites/ui/background.png"));

        interfaceGraficasMenu.add(new InterfaceGrafica() {
            @Override
            public void renderizar(Batch gameBatch, Vector2 posicaoMouse, boolean pressionado) {
                gameBatch.draw(imagemFundo, 0, 0, 512, 512);
                gameBatch.draw(imagemFundo, 512, 0, 512, 512);
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

        jogadorSprites = new Sprite[]{
                new Sprite(new Texture("sprites/ship/base/full.png")),
                new Sprite(new Texture("sprites/ship/base/damage1.png")),
                new Sprite(new Texture("sprites/ship/base/damage2.png")),
                new Sprite(new Texture("sprites/ship/base/damage3.png"))
        };

        final var jogadorDestruicaoTexture = new Texture("sprites/ship/base/destruction.png");
        // 8 sprites de 64x64
        jogadorDestruicaoSprites = new Sprite[]{new Sprite(jogadorDestruicaoTexture, 0, 0, 64, 64), new Sprite(jogadorDestruicaoTexture, 64, 0, 64, 64), new Sprite(jogadorDestruicaoTexture, 128, 0, 64, 64), new Sprite(jogadorDestruicaoTexture, 192, 0, 64, 64), new Sprite(jogadorDestruicaoTexture, 256, 0, 64, 64), new Sprite(jogadorDestruicaoTexture, 320, 0, 64, 64), new Sprite(jogadorDestruicaoTexture, 384, 0, 64, 64), new Sprite(jogadorDestruicaoTexture, 448, 0, 64, 64)};

        final var inimigoBasicoDestruicaoTexture = new Texture("sprites/enemies/destruction/fighter.png");
        // 8 sprites de 64x64
        inimigoBasicoDestruicaoSprites = new Sprite[]{new Sprite(inimigoBasicoDestruicaoTexture, 0, 0, 64, 64), new Sprite(inimigoBasicoDestruicaoTexture, 64, 0, 64, 64), new Sprite(inimigoBasicoDestruicaoTexture, 128, 0, 64, 64), new Sprite(inimigoBasicoDestruicaoTexture, 192, 0, 64, 64), new Sprite(inimigoBasicoDestruicaoTexture, 256, 0, 64, 64), new Sprite(inimigoBasicoDestruicaoTexture, 320, 0, 64, 64), new Sprite(inimigoBasicoDestruicaoTexture, 384, 0, 64, 64), new Sprite(inimigoBasicoDestruicaoTexture, 448, 0, 64, 64)};
        final var bomberInimigoDestruicaoTexture = new Texture("sprites/enemies/destruction/bomber.png");
        // 8 sprites de 64x64
        bomberInimigoDestruicaoSprites = new Sprite[]{new Sprite(bomberInimigoDestruicaoTexture, 0, 0, 64, 64), new Sprite(bomberInimigoDestruicaoTexture, 64, 0, 64, 64), new Sprite(bomberInimigoDestruicaoTexture, 128, 0, 64, 64), new Sprite(bomberInimigoDestruicaoTexture, 192, 0, 64, 64), new Sprite(bomberInimigoDestruicaoTexture, 256, 0, 64, 64), new Sprite(bomberInimigoDestruicaoTexture, 320, 0, 64, 64), new Sprite(bomberInimigoDestruicaoTexture, 384, 0, 64, 64), new Sprite(bomberInimigoDestruicaoTexture, 448, 0, 64, 64)};
        final var suporteInimigoDestruicaoTexture = new Texture("sprites/enemies/destruction/support.png");
        // 9 sprites de 64x64
        suporteInimigoDestruicaoSprites = new Sprite[]{new Sprite(suporteInimigoDestruicaoTexture, 0, 0, 64, 64), new Sprite(suporteInimigoDestruicaoTexture, 64, 0, 64, 64), new Sprite(suporteInimigoDestruicaoTexture, 128, 0, 64, 64), new Sprite(suporteInimigoDestruicaoTexture, 192, 0, 64, 64), new Sprite(suporteInimigoDestruicaoTexture, 256, 0, 64, 64), new Sprite(suporteInimigoDestruicaoTexture, 320, 0, 64, 64), new Sprite(suporteInimigoDestruicaoTexture, 384, 0, 64, 64), new Sprite(suporteInimigoDestruicaoTexture, 448, 0, 64, 64), new Sprite(suporteInimigoDestruicaoTexture, 512, 0, 64, 64)};

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.ogg"));
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }

    private void novoJogo() {
        rodada = 0;
        timerChefe = 0;
        intercalarTiposInimigosChefe = 0;
        tempoAtualEntreRodadas = 0f;
        intervaloAtualEntreAsteroides = 0f;
        parteAtual = 0;
        podeTrocarArma = false;
        podeUpgrade = false;

        entidadesAliadas.clear();
        projeteisAliados.clear();
        entidadesInimigas.clear();
        projeteisInimigos.clear();
        asteroides.clear();

        final var colisao = new Vector2(20, 20);
        final var posicao = new Vector2(LARGURA_JOGO / 2f - colisao.x / 2f, 75);

        naveAliada = new NaveAliada(projeteisAliados, jogadorSprites, posicao, colisao, 100, jogadorDestruicaoSprites);
        entidadesAliadas.add(naveAliada);

        armas = new Arma[]{
                CriadorArmas.gun(naveAliada, projeteisAliados),
                CriadorArmas.cannon(naveAliada, projeteisAliados),
                CriadorArmas.zapper(naveAliada, projeteisAliados),
                CriadorArmas.rockets(naveAliada, projeteisAliados)
        };

        for (int i = 0; i < armas.length; i++) {
            if (armas[i].getArmaSprite() != null) {
                armas[i].getArmaSprite().setPosition(escolherArmaOffset + i * escolherArmaSeparar + armas[i].getPosicaoRelativa().x + jogadorSprites[0].getWidth() / 2 - armas[i].getArmaSprite().getWidth() / 2, escolherArmaY + armas[i].getPosicaoRelativa().y + jogadorSprites[0].getHeight() / 2 - armas[i].getArmaSprite().getHeight() / 2);
                armas[i].getArmaSprite().setRotation(0);
            }
            armas[i].getArmaEngineSprite().setPosition(escolherArmaOffset + i * escolherArmaSeparar + armas[i].getPosicaoRelativaEngine().x + jogadorSprites[0].getWidth() / 2 - armas[i].getArmaEngineSprite().getWidth() / 2, escolherArmaY + armas[i].getPosicaoRelativaEngine().y + jogadorSprites[0].getHeight() / 2 - armas[i].getArmaEngineSprite().getHeight() / 2);
            armas[i].getArmaEngineSprite().setRotation(0);

            armas[i].getSuporteSprite().setPosition(escolherArmaOffset + i * escolherArmaSeparar + armas[i].getPosicaoRelativaSuporte().x + jogadorSprites[0].getWidth() / 2 - armas[i].getSuporteSprite().getWidth() / 2, escolherArmaY + armas[i].getPosicaoRelativaSuporte().y + jogadorSprites[0].getHeight() / 2 - armas[i].getSuporteSprite().getHeight() / 2);
            armas[i].getSuporteSprite().setRotation(0);
        }
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }

    public void tickUpdateEntidade(Entidade entidade, Array<Entidade> entidades, int index) {
        entidade.atualizarFisica(1.0f / atualizacoesPorSegundo);
        if (entidade.estaCompletamenteDestruido()) {
            entidades.removeIndex(index);
        } else if (!(entidade instanceof Projetil) && !(entidade instanceof Asteroide)) {
            if (entidade.getPosicao().y + entidade.getColisao().y > ALTURA_JOGO && (!(entidade instanceof Inimigo) || !((Inimigo) entidade).getPodeMoverCimaJogo())) {
                entidade.setPosicao(new Vector2(entidade.getPosicao().x, ALTURA_JOGO - entidade.getColisao().y));
                entidade.setVelocidade(new Vector2(entidade.getVelocidade().x, 0));
            } else if (entidade.getPosicao().y < 0) {
                entidade.setPosicao(new Vector2(entidade.getPosicao().x, 0));
                entidade.setVelocidade(new Vector2(entidade.getVelocidade().x, 0));
            } else if (entidade instanceof Inimigo && ((Inimigo) entidade).getPodeMoverCimaJogo() && entidade.getPosicao().y + entidade.getColisao().y < ALTURA_JOGO) {
                ((Inimigo) entidade).desativarMovimentacaoCimaJogo();
            }
            if (entidade.getPosicao().x + entidade.getColisao().x > LARGURA_JOGO) {
                entidade.setPosicao(new Vector2(LARGURA_JOGO - entidade.getColisao().x, entidade.getPosicao().y));
                entidade.setVelocidade(new Vector2(0, entidade.getVelocidade().y));
            } else if (entidade.getPosicao().x < 0) {
                entidade.setPosicao(new Vector2(0, entidade.getPosicao().y));
                entidade.setVelocidade(new Vector2(0, entidade.getVelocidade().y));
            }
        } else if (entidade instanceof Asteroide) {

        }
    }

    public void tickUpdateEntidadeAliada(Entidade entidade, int index, Vector2 posicaoMouse) {
        ((Nave) entidade).setOlharPara(posicaoMouse);
        this.tickUpdateEntidade(entidade, entidadesAliadas, index);
        entidade.checarColisao(entidadesInimigas, 1.0f / atualizacoesPorSegundo);
        entidade.checarColisao(projeteisInimigos, 1.0f / atualizacoesPorSegundo);
        entidade.checarColisao(asteroides, 1.0f / atualizacoesPorSegundo);
    }

    public void tickUpdateEntidadeInimiga(Entidade entidade, int index) {
        if (entidade instanceof Suporte) {
            if (entidadesInimigas.get(0) instanceof Chefe) {
                ((Suporte) entidade).setOlharPara(entidadesInimigas.get(0).getPosicao().cpy().add(entidadesInimigas.get(0).getColisao().cpy().scl(0.5f)));
            }
        } else {
            ((Inimigo) entidade).setOlharPara(naveAliada.getPosicao().cpy().add(naveAliada.getColisao().cpy().scl(0.5f)));
        }
        ((Inimigo) entidade).atirar(naveAliada);
        this.tickUpdateEntidade(entidade, entidadesInimigas, index);
        entidade.checarColisao(entidadesAliadas, 1.0f / atualizacoesPorSegundo);
        entidade.checarColisao(projeteisAliados, 1.0f / atualizacoesPorSegundo);
    }

    public void tickUpdateEntidadeTemporaria(Entidade entidade, Array<Entidade> entidadesTemporarias, int index) {
        if (entidade.getPosicao().y > ALTURA_JOGO) {
            entidadesTemporarias.removeIndex(index);
            return;
        } else if (entidade.getPosicao().y < 0 - entidade.getColisao().y) {
            entidadesTemporarias.removeIndex(index);
            return;
        } else if (entidade.getPosicao().x > LARGURA_JOGO) {
            entidadesTemporarias.removeIndex(index);
            return;
        } else if (entidade.getPosicao().x < 0 - entidade.getColisao().x) {
            entidadesTemporarias.removeIndex(index);
            return;
        }
        this.tickUpdateEntidade(entidade, entidadesTemporarias, index);
    }

    public void tickUpdateProjetilAliado(Entidade entidade, int index) {
        this.tickUpdateEntidadeTemporaria(entidade, projeteisAliados, index);
        entidade.checarColisao(entidadesInimigas, 1.0f / atualizacoesPorSegundo);
        entidade.checarColisao(asteroides, 1.0f / atualizacoesPorSegundo);
    }

    public void tickUpdateProjetilInimigo(Entidade entidade, int index) {
        this.tickUpdateEntidadeTemporaria(entidade, projeteisInimigos, index);
        entidade.checarColisao(entidadesAliadas, 1.0f / atualizacoesPorSegundo);
    }

    public void tickUpdateAsteroides(Entidade entidade, int index) {
        this.tickUpdateEntidadeTemporaria(entidade, asteroides, index);
        entidade.checarColisao(projeteisAliados, 1.0f / atualizacoesPorSegundo);
    }

    public void tickUpdate(Vector2 posicaoMouse) {
        for (int i = 0; i < asteroides.size; i++) {
            tickUpdateAsteroides(asteroides.get(i), i);
        }
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

    public void renderizarEntidade(Entidade entidade, Vector2 posicaoMouse) {
        final var posicao = entidade.getPosicao();
        if (posicao == null) return;
        if (entidade.getImagem() != null)
            entidade.getImagem().setPosition(posicao.x - entidade.getImagem().getWidth() / 2 + entidade.getColisao().x / 2, posicao.y - entidade.getImagem().getHeight() / 2 + entidade.getColisao().y / 2);
        Sprite armaSprite = null;
        Sprite armaEngineSprite = null;
        Sprite armaSuporteSprite = null;
        Arma arma = null;
        if (entidade instanceof Nave && entidade.getImagem() != null) {
            entidade.getImagem().setRotation(((Nave) entidade).getRotacao());
            arma = ((Nave) entidade).getArma();
            if (arma != null && !((Nave) entidade).estaDestruido()) {
                armaSprite = arma.getArmaSprite();
                armaEngineSprite = arma.getArmaEngineSprite();
                armaSuporteSprite = arma.getSuporteSprite();
            }
            if (armaSprite != null) {
                armaSprite.setPosition(posicao.x + arma.getPosicaoRelativa().x - armaSprite.getWidth() / 2 + entidade.getColisao().x / 2, posicao.y + arma.getPosicaoRelativa().y - armaSprite.getHeight() / 2 + entidade.getColisao().y / 2);
                armaSprite.setRotation(((Nave) entidade).getRotacao());
                armaSprite.draw(gameBatch);

                armaEngineSprite.setPosition(posicao.x + arma.getPosicaoRelativaEngine().x - armaEngineSprite.getWidth() / 2 + entidade.getColisao().x / 2, posicao.y + arma.getPosicaoRelativaEngine().y - armaEngineSprite.getHeight() / 2 + entidade.getColisao().y / 2);
                armaEngineSprite.setRotation(((Nave) entidade).getRotacao());
                armaEngineSprite.draw(gameBatch);
            }
        } else if (entidade instanceof Projetil) {
            entidade.getImagem().setRotation(entidade.getVelocidade().angleDeg() - 90);
        }
        if (entidade.getImagem() != null) entidade.getImagem().draw(gameBatch);
        if (entidade instanceof Inimigo && !((Inimigo) entidade).estaDestruido()) {
            gameBatch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(posicao.x, posicao.y - 10, entidade.getColisao().x * ((Inimigo) entidade).getVida() / ((Inimigo) entidade).getVidaMaxima(), 2);
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rect(posicao.x, posicao.y - 10, entidade.getColisao().x, 2);
            shapeRenderer.end();
            gameBatch.begin();
        }

        if (armaSuporteSprite != null) {
            armaSuporteSprite.setPosition(posicao.x + arma.getPosicaoRelativaSuporte().x - armaSuporteSprite.getWidth() / 2 + entidade.getColisao().x / 2, posicao.y + arma.getPosicaoRelativaSuporte().y - armaSuporteSprite.getHeight() / 2 + entidade.getColisao().y / 2);
            armaSuporteSprite.setRotation(((Nave) entidade).getRotacao());
            armaSuporteSprite.draw(gameBatch);
        }

        if (renderizarCaixaDeColisao) {
            gameBatch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(entidade.getPosicao().x, entidade.getPosicao().y, entidade.getColisao().x, entidade.getColisao().y);
            shapeRenderer.end();
            gameBatch.begin();
        }
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            trocarArmaDebug = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
            stepFrames = !stepFrames;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
            steppingFrame = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            switch (cenaAtual) {
                case MENU:
                    Gdx.app.exit();
                    break;
                case EM_JOGO:
                    if (!naveAliada.estaCompletamenteDestruido()) {
                        pausado = !pausado;
                    } else {
                        cenaAtual = Cena.MENU;
                    }
                    break;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (cenaAtual == Cena.EM_JOGO) {
                if (naveAliada.estaCompletamenteDestruido()) {
                    novoJogo();
                }
            }
        }

        cameraDoJogo.update();
        gameBatch.setProjectionMatrix(cameraDoJogo.combined);
        gameBatch.begin();

        float TEMPO_ENTRE_RODADAS = 4f;
        switch (cenaAtual) {
            case MENU:
                for (InterfaceGrafica interfaceGrafica : interfaceGraficasMenu) {
                    interfaceGrafica.renderizar(gameBatch, posicaoMouse, Gdx.input.isButtonPressed(Input.Buttons.LEFT));
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    novoJogo();
                    cenaAtual = Cena.EM_JOGO;
                }
                break;
            case EM_JOGO:
                float deltaTime = Gdx.graphics.getDeltaTime();

                tempoDesdeUltimaAtualizacao += deltaTime;

                while (tempoDesdeUltimaAtualizacao >= 1f / atualizacoesPorSegundo) {
                    if (!pausado && (!stepFrames || steppingFrame)) {
                        if (entidadesInimigas.size > 0 && entidadesInimigas.get(0) instanceof Chefe) {
                            timerChefe += 1f / atualizacoesPorSegundo;
                            if (rodada % RODADA_CHEFE == 0 && timerChefe >= 10f && !naveAliada.estaDestruido()) {
                                timerChefe -= 10f;
                                for (int i = 0; i < rodada / RODADA_CHEFE + 1; i++) {
                                    switch (intercalarTiposInimigosChefe) {
                                        case 0:
                                            final var suporte = new Suporte((Chefe) entidadesInimigas.get(0), suporteInimigoSprite, new Vector2(entidadesInimigas.get(0).getPosicao().x - 14 + (entidadesInimigas.get(0).getColisao().x / 2) + MathUtils.random(-16, 16), entidadesInimigas.get(0).getPosicao().y + entidadesInimigas.get(0).getColisao().y / 2 + +MathUtils.random(-20, 20)), new Vector2(48, 48), 15 * (1 + 0.01f * (rodada - 1)), 100f, suporteInimigoDestruicaoSprites, 5f, 1000f, 900f);
                                            entidadesInimigas.add(suporte);
                                            break;
                                        case 1:
                                            final var bomber = new Bomber(naveAliada, bomberInimigoSprite, new Vector2(entidadesInimigas.get(0).getPosicao().x - 12 + (entidadesInimigas.get(0).getColisao().x / 2) + MathUtils.random(-12, 12), entidadesInimigas.get(0).getPosicao().y + entidadesInimigas.get(0).getColisao().y / 2 + +MathUtils.random(-4, 4)), new Vector2(24, 24), 25 * (1 + 0.01f * (rodada - 1)), 100f, bomberInimigoDestruicaoSprites, 3f, 600f, 700f, rodada - 1);
                                            entidadesInimigas.add(bomber);
                                            break;
                                        case 2:
                                            final var combatente = new Combatente(inimigoBasicoSprite, new Vector2(entidadesInimigas.get(0).getPosicao().x - 12 + (entidadesInimigas.get(0).getColisao().x / 2) + MathUtils.random(-12, 12), entidadesInimigas.get(0).getPosicao().y + entidadesInimigas.get(0).getColisao().y / 2 + +MathUtils.random(-4, 4)), new Vector2(24, 24), 25 * (1 + 0.01f * (rodada - 1)), 100f, inimigoBasicoDestruicaoSprites, 0.1f, 1000f, 175f);
                                            combatente.trocarArma(CriadorArmas.basica(combatente, projeteisInimigos, rodada - 1));
                                            entidadesInimigas.add(combatente);
                                            break;
                                    }
                                }
                                intercalarTiposInimigosChefe++;
                                if (intercalarTiposInimigosChefe > 2) intercalarTiposInimigosChefe = 0;
                            }
                        }
                        if (entidadesInimigas.size > 0) {
                            intervaloAtualEntreAsteroides += 1f / atualizacoesPorSegundo;
                            while (intervaloAtualEntreAsteroides >= INTERVALO_ENTRE_ASTEROIDES) {
                                intervaloAtualEntreAsteroides -= INTERVALO_ENTRE_ASTEROIDES;
                                final var asteroid = new Asteroide(LARGURA_JOGO, ALTURA_JOGO);
                                asteroides.add(asteroid);
                            }
                        }
                        tickUpdate(posicaoMouse);
                        if (!pausado && (!stepFrames || steppingFrame)) naveAliada.atualizarQuadro(deltaTime);
                        for (Entidade entidade : asteroides) {
                            entidade.atualizarQuadro(deltaTime);
                        }
                        for (Entidade entidade : entidadesInimigas) {
                            entidade.atualizarQuadro(deltaTime);
                        }
                        for (Entidade entidade : projeteisAliados) {
                            entidade.atualizarQuadro(deltaTime);
                        }
                        for (Entidade entidade : projeteisInimigos) {
                            entidade.atualizarQuadro(deltaTime);
                        }
                        if (entidadesInimigas.size == 0 && !podeTrocarArma && !podeUpgrade) {
                            tempoAtualEntreRodadas += 1f / atualizacoesPorSegundo;
                        }
                        steppingFrame = false;
                    }
                    tempoDesdeUltimaAtualizacao -= 1f / atualizacoesPorSegundo;
                }

                if (!pausado && (!stepFrames || steppingFrame)) parteAtual += deltaTime * 10f;
                interfaceGraficaFundo.renderizar(gameBatch, posicaoMouse, Gdx.input.isButtonPressed(Input.Buttons.LEFT));

                if (entidadesInimigas.size == 0) {
                    podeTrocarArma = naveAliada.querendoTrocarArma();
                    podeUpgrade = naveAliada.querendoUpgrade();
                    if (!podeTrocarArma && !podeUpgrade && tempoAtualEntreRodadas >= TEMPO_ENTRE_RODADAS) {
                        tempoAtualEntreRodadas = 0f;
                        rodada++;
                        this.naveAliada.curar(10);
                        for (int i = 0; i < (rodada == 1 ? 1 : (rodada - 1) * 4 / 3); i++) {
                            if (rodada % RODADA_CHEFE == 0) {
                                Chefe chefe = new Chefe(LARGURA_JOGO, ALTURA_JOGO, 750 + (rodada / RODADA_CHEFE) * 450);
                                chefe.trocarArma(CriadorArmas.armaOnda(chefe, projeteisInimigos, rodada / RODADA_CHEFE - 1));
                                entidadesInimigas.add(chefe);
                                break;
                            } else {
                                if (i % 2 == 0) {
                                    final var inimigo = new Combatente(inimigoBasicoSprite, new Vector2(MathUtils.random(LARGURA_JOGO), ALTURA_JOGO + MathUtils.random(24, 100)), new Vector2(24, 24), 25 * (1 + 0.01f * (rodada - 1)), 100f, inimigoBasicoDestruicaoSprites, 0.1f, 1000f, 175f);
                                    inimigo.trocarArma(CriadorArmas.basica(inimigo, projeteisInimigos, rodada - 1));
                                    entidadesInimigas.add(inimigo);
                                } else {
                                    entidadesInimigas.add(new Bomber(naveAliada, bomberInimigoSprite, new Vector2(MathUtils.random(LARGURA_JOGO), ALTURA_JOGO + MathUtils.random(24, 100)), new Vector2(24, 24), 25 * (1 + 0.01f * (rodada - 1)), 100f, bomberInimigoDestruicaoSprites, 3f, 600f, 700f, rodada - 1));
                                }
                            }
                        }
                    }
                }
                shapeRenderer.setProjectionMatrix(cameraDoJogo.combined);
                for (Entidade entidade : projeteisInimigos) {
                    renderizarEntidade(entidade, posicaoMouse);
                }
                for (Entidade entidade : projeteisAliados) {
                    renderizarEntidade(entidade, posicaoMouse);
                }
                for (Entidade entidade : asteroides) {
                    renderizarEntidade(entidade, posicaoMouse);
                }
                for (Entidade entidade : entidadesInimigas) {
                    renderizarEntidade(entidade, posicaoMouse);
                }
                renderizarEntidade(naveAliada, posicaoMouse);
                var xInput = Gdx.input.isKeyPressed(Input.Keys.D) ? 1 : Gdx.input.isKeyPressed(Input.Keys.A) ? -1 : 0;
                var yInput = Gdx.input.isKeyPressed(Input.Keys.W) ? 1 : Gdx.input.isKeyPressed(Input.Keys.S) ? -1 : 0;
                if (!pausado && (!stepFrames || steppingFrame)) {
                    naveAliada.setMovimento(new Vector2(xInput, yInput));
                    if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                        naveAliada.atirar(posicaoMouse);
                    }
                }
                if (!naveAliada.estaDestruido())
                    fontePadrao.draw(gameBatch, "Vida: " + naveAliada.textoVida(), 20, ALTURA_JOGO - 20);
                fontePadrao.draw(gameBatch, "XP: " + naveAliada.getXp(), 20, ALTURA_JOGO - 60);
                fontePadrao.draw(gameBatch, "Nível: " + naveAliada.getNivel(), 20, ALTURA_JOGO - 100);
                if (pausado) {
                    interfaceGraficaPausado.renderizar(gameBatch, posicaoMouse, Gdx.input.isButtonPressed(Input.Buttons.LEFT));
                }
                if (entidadesInimigas.size == 0 && tempoAtualEntreRodadas < TEMPO_ENTRE_RODADAS && !podeTrocarArma) {
                    fontePadrao.draw(gameBatch, "Rodada: " + (rodada < 9 ? "0" : "") + (rodada + 1), LARGURA_JOGO / 2.0f - 60, ALTURA_JOGO / 2.0f + 20);
                    if ((rodada + 1) % RODADA_CHEFE == 0) {
                        fontePadrao.draw(gameBatch, "Chefe", LARGURA_JOGO / 2.0f - 30, ALTURA_JOGO / 2.0f - 20);
                    }
                }
                if (naveAliada.estaCompletamenteDestruido()) {
                    interfaceGraficaGameOver.renderizar(gameBatch, posicaoMouse, Gdx.input.isButtonPressed(Input.Buttons.LEFT));
                    interfaceGraficaPressioneParaContinuar.renderizar(gameBatch, posicaoMouse, Gdx.input.isButtonPressed(Input.Buttons.LEFT));
                } else {
                    if (((podeTrocarArma && naveAliada.querendoTrocarArma()))) {
                        interfaceGraficaEscolherArmaFundo.renderizar(gameBatch, posicaoMouse, Gdx.input.isButtonPressed(Input.Buttons.LEFT));
                        fontePadrao.draw(gameBatch, "Escolha a arma: ", LARGURA_JOGO / 2.0f - 90, ALTURA_JOGO / 2.0f + 20);
                        for (int i = 0; i < armas.length; i++) {
                            if (armas[i].getArmaSprite() != null) armas[i].getArmaSprite().draw(gameBatch);
                            armas[i].getArmaEngineSprite().draw(gameBatch);
                            gameBatch.draw(jogadorSprites[0], escolherArmaOffset + i * escolherArmaSeparar, escolherArmaY);
                            armas[i].getSuporteSprite().draw(gameBatch);
                            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                                if (armas[i].getArmaSprite().getBoundingRectangle().contains(posicaoMouse)) {
                                    naveAliada.trocarArma(armas[i]);
                                    trocarArmaDebug = false;
                                }
                            }
                        }
                    } else if (trocarArmaDebug || (podeUpgrade && naveAliada.querendoUpgrade())) {
                        interfaceGraficaEscolherArmaFundo.renderizar(gameBatch, posicaoMouse, Gdx.input.isButtonPressed(Input.Buttons.LEFT));
                        fontePadrao.draw(gameBatch, "Escolha o upgrade: ", LARGURA_JOGO / 2.0f - 90, ALTURA_JOGO / 2.0f + 20);
                        fontePadrao.setColor(Color.RED);
                        fontePadrao.draw(gameBatch, "Upgrade de vida máxima +50 (atual: " + naveAliada.getVidaMaxima() + ")", LARGURA_JOGO / 2.0f - 225, ALTURA_JOGO / 2.0f - 20);
                        fontePadrao.draw(gameBatch, "Upgrade de dano +2 (atual: " + naveAliada.getDano() + ")", LARGURA_JOGO / 2.0f - 225, ALTURA_JOGO / 2.0f - 60);
                        fontePadrao.draw(gameBatch, "Diminuir tempo de recarga em 10% (atual: " + MathUtils.round(naveAliada.getTempoRecarga() * 100) + ")", LARGURA_JOGO / 2.0f - 225, ALTURA_JOGO / 2.0f - 100);
                        fontePadrao.setColor(Color.WHITE);
                        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                            if (posicaoMouse.x > 100 && posicaoMouse.x < LARGURA_JOGO - 200) {
                                if (posicaoMouse.y > ALTURA_JOGO / 2.0f - 50 && posicaoMouse.y < ALTURA_JOGO / 2.0f - 20) {
                                    naveAliada.aumentarVidaMaxima(50);
                                    trocarArmaDebug = false;
                                } else if (posicaoMouse.y > ALTURA_JOGO / 2.0f - 90 && posicaoMouse.y < ALTURA_JOGO / 2.0f - 60) {
                                    naveAliada.aumentarDano(2);
                                    trocarArmaDebug = false;
                                } else if (posicaoMouse.y > ALTURA_JOGO / 2.0f - 130 && posicaoMouse.y < ALTURA_JOGO / 2.0f - 100) {
                                    naveAliada.diminuirTempoRecarga(0.1f);
                                    trocarArmaDebug = false;
                                }
                            }
                        }
                    }
                }
                break;
        }

        gameBatch.end();
    }

    @Override
    public void dispose() {
        gameBatch.dispose();
        fontePadrao.dispose();
        shapeRenderer.dispose();
        music.stop();
        music.dispose();
    }
}
