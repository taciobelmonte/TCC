<?php
    if($_SERVER['PHP_SELF'] != '/tcc/index.php'){?>
        <div class="center brok">
            <a class="btn white footer" id="home" href="index.php">Voltar para home</a>
            <p class="footer">Desenvolvido por: Tácio Belmonte</p>
        </div>
    <?php }
?>
<script src="//cdnjs.cloudflare.com/ajax/libs/paho-mqtt/1.0.1/mqttws31.min.js" type="text/javascript"></script>
<script src="<?php echo get_stylesheet_directory_uri()?>/resources/js/main.js" type="text/javascript"></script>
<script type="text/javascript" src="//www.gstatic.com/charts/loader.js"></script>
</body>
</html>
