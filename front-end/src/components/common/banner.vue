<script>
</script>

<template>
<div class="banner-header"   >
    <h1 style="margin-left: 20px;" class="banner-header-h1">Grace in Nature</h1>
    <p  style="margin-left: 20px;" class="banner-header-p">Better everyday, kinder to the planet.</p>
</div>

</template>

<style>


.banner-header{
    background-image: url('/src/pictures/bannerBrown.jpg');
    width:2400px;
    height:300px;
    


    background-size: cover; /* 關鍵：確保圖片填滿且不變形 */
    background-position: top; /* 圖片置中 */
    background-repeat: no-repeat; 
    background-attachment: fixed; /* 讓背景圖片固定在視口 */
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