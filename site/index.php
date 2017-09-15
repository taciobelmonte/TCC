<?php get_header();?>

    <div class="container center">
    <?php
        if(!is_user_logged_in()){?>

            <div class="row col s6 m6 l6 center">

                <div id="login-register-password">

                    <?php global $user_ID, $user_identity; if (!$user_ID) { ?>

                        <div class="tab_container_logincenter col s12 m12 l12">
                            <div id="tab1_login" class="tab_content_login center">

                                <?php $register = $_GET['register']; $reset = $_GET['reset']; if ($register == true) { ?>

                                    <h3>Successo!</h3>
                                    <p>Verifique seu e-mail e senha e retorne para logar.</p>

                                    <a href="<?php echo site_url()?>" class="btn"> Retornar para Home </a>

                                <?php } elseif ($reset == true) { ?>

                                    <h3>Successo!</h3>
                                    <p>Verifique seu e-mail para resetar sua senha.</p>

                                <?php } else { ?>

                                    <h3>Já possui uma conta?</h3>

                                    <div class="col s12">&nbsp;</div>
                                    <div class="col s12 m4 l2">&nbsp;</div>
                                    <form method="post" action="<?php bloginfo('url') ?>/wp-login.php" class="wp-user-form col s12 m4 l8">
                                        <div class="input-field col s6 username">
                                            <label for="user_login"><?php _e('Username'); ?>: </label>
                                            <input type="text" name="log" value="<?php echo esc_attr(stripslashes($user_login)); ?>" size="20" id="user_login" tabindex="11" />
                                        </div>
                                        <div class="input-field col s6 password">
                                            <label for="user_pass"><?php _e('Password'); ?>: </label>
                                            <input type="password" name="pwd" value="" size="20" id="user_pass" tabindex="12" />
                                        </div>
                                        <div class="clearfix"></div>
                                        <div class="input-field login_fields center">
                                            <?php do_action('login_form');?>
                                            <input type="hidden" name="redirect_to" value="<?php echo $_SERVER['REQUEST_URI']; ?>" />
                                            <input type="submit" name="user-submit" value="<?php _e('Login'); ?>" tabindex="14" class="user-submit center btn" />
                                            <input type="hidden" name="user-cookie" value="1" />
                                        </div>
                                    </form>

                                    <div class="col s12 m4 l2">&nbsp;</div>

                                <?php } ?>

                            </div>
                            <br />
                            <br />

                            <?php if ($register != true){?>

                                <div id="tab2_login" class="tab_content_login  col s12 m12 l12">
                                    <h3>Registrar!</h3>

                                    <div class="col s12">&nbsp;</div>
                                    <div class="col s12 m4 l2">&nbsp;</div>

                                    <form method="post" action="<?php echo site_url('wp-login.php?action=register', 'login_post') ?>" class="wp-user-form col s12 m4 l8">
                                        <div class="input-field col s6 username">
                                            <label for="user_login"><?php _e('Username'); ?>: </label>
                                            <input type="text" name="user_login" value="<?php echo esc_attr(stripslashes($user_login)); ?>" size="20" id="user_login" tabindex="101" />
                                        </div>
                                        <div class="password input-field col s6">
                                            <label for="user_email"><?php _e('Your Email'); ?>: </label>
                                            <input type="text" name="user_email" value="<?php echo esc_attr(stripslashes($user_email)); ?>" size="25" id="user_email" tabindex="102" />
                                        </div>

                                        <div class="clearfix"></div>

                                        <div class="login_fields input-field center">
                                            <?php do_action('register_form');?>
                                            <input type="submit" name="user-submit" value="<?php _e('Cadastrar!'); ?>" class="user-submit btn" tabindex="103" />
                                            <?php $register = $_GET['register']; if($register == true) {
                                                echo '<p>Verifique seu e-mail e logue com a senha!</p>';?>
                                            <?php }
                                            ?>
                                            <input type="hidden" name="redirect_to" value="<?php echo $_SERVER['REQUEST_URI']; ?>?register=true" />
                                            <input type="hidden" name="user-cookie" value="1" />
                                        </div>
                                    </form>
                                    <div class="col s12 m4 l2">&nbsp;</div>
                                </div>

                            <?php } ?>



                    <?php } else { // is logged in ?>

                        <div class="sidebox">
                            <h3>Welcome, <?php echo $user_identity; ?></h3>
                            <div class="usericon">
                                <?php global $userdata; echo get_avatar($userdata->ID, 60); ?>

                            </div>
                            <div class="userinfo">
                                <p>You&rsquo;re logged in as <strong><?php echo $user_identity; ?></strong></p>
                                <p>
                                    <a href="<?php echo wp_logout_url('index.php'); ?>">Log out</a> |
                                    <?php if (current_user_can('manage_options')) {
                                        echo '<a href="' . admin_url() . '">' . __('Admin') . '</a>'; } else {
                                        echo '<a href="' . admin_url() . 'profile.php">' . __('Profile') . '</a>'; } ?>

                                </p>
                            </div>
                        </div>

                    <?php } ?>
                </div>

            </div>




        <?php }else{?>
            <main>
                <div class="">
                    <div class="row">
                        <div class="col s12 m12 l12">
                            <div id="status_io" class="center"></div>
                            <div class="center">
                                <br /><br /><br />
                                <h4><b>Todas as Estações</b></h4>

                                <div class="center-block">
                                    <div class="oximetria">
                                        <a href="<?php echo site_url()?>/oximetria">
                                            <img width="150" src="<?php echo get_stylesheet_directory_uri()?>/resources/img/oximetria.png" alt="Oximetria"/>
                                            <br />
                                            Oximetria
                                        </a>
                                    </div>
                                    <div class="batimentos-cardiacos">
                                        <a href="<?php echo site_url()?>/batimentos-cardiacos">
                                            <img width="150" src="<?php echo get_stylesheet_directory_uri()?>/resources/img/heartbeating.png" alt="Batimentos Cardíacos"/>
                                            <br />
                                            Frequência cardíaca
                                        </a>
                                    </div>
                                </div>
                                <div id="debug"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        <?php }
    ?>
    </div>
<?php get_footer();?>