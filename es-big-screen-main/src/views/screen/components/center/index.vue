<template>
    <div class="es-center">
        <div class="es-center-top">
            <AMap />
        </div>
        <Bottom :systemId="props.systemId" :role="props.role" />

    </div>
</template>

<script setup lang='ts'>
import Map from './Map.vue'
import AMap from './AMap.vue'
import Bottom from './Bottom.vue'
import { ref, onMounted, computed, defineProps } from 'vue'
const props = defineProps({
    systemId: String,
    role: String
})

</script>

<style lang='scss' scoped>
.es-center {
    position: relative;
    // width: 100%;
    height: 1000px;
    padding: 0 16px;
    animation: slideAndFade 1.5s;

    .es-center-top {
        height: calc(100% - 150px);
    }
}

@keyframes slideAndFade {
    0% {
        transform: translateY(218px);
        opacity: 0;
    }

    100% {
        transform: translateX(0);
        opacity: 1;
    }
}
</style>
