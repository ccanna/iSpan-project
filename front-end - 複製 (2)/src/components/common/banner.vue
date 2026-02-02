<script>
</script>

<template>
<div class="banner-header"   >
    <h1 class="banner-header-h1">Shop in style</h1>
    <p class="banner-header-p">With this shop hompeage template</p>
</div>

</template>

<style>
/* .header {
    background: #1f2428;
    padding: 30px 0;
    text-align: center;
    color: #fff;
} */

.banner-header{
    background-image: url('/src/pictures/bannerBrown.jpg');
    width:2400px;
    height:300px;
    background-size: cover; /* 關鍵：確保圖片填滿且不變形 */
    background-position: top; /* 圖片置中 */
    background-repeat: no-repeat;
}

.header-cart {
    background: #1f2428;
    padding: 15px 0;
    text-align: center;
    color: #f0ecec;
}


.banner-header-h1 {
    font-size: 64px;
    font-weight: 700;
    margin: 0;
    color: rgba(255, 255, 255, 0.858);

}


.banner-header-p {
    margin: 12px 0 0;
    font-size: 30px;
    color: rgba(255, 255, 255, 0.51);
}
</style>