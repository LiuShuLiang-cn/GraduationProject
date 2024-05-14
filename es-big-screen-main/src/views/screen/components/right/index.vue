<template>
    <div ref="containerRef">
        <component v-for="item in components" :key="item.name" :is="item.component" v-bind="user"
            class="es-screen-right-item">
            {{ item.name }}
        </component>
        <!-- <component class="es-screen-right-item">
            <Right1 />
            <Right2 />
            <Right3 />
        </component> -->
    </div>
</template>

<script setup lang='ts'>
import { shallowRef, defineProps } from 'vue'
import { useSortable } from '@/utils/useSortable'
import Right1 from './Right1.vue'
import Right2 from './Right2.vue'
import Right3 from './Right3.vue'
const components = shallowRef([
    { name: 'right1', component: Right1 },
    { name: 'right2', component: Right2 },
    { name: 'right3', component: Right3 }
])
const props = defineProps({
    systemId: String,
    role: String
})
const user = {
    systemId: props.systemId,
    role: props.role
}
const { containerRef } = useSortable(components)
</script>

<style lang='scss' scoped>
.es-screen-right-item {
    width: 100%;
    height: 320px;
    background-color: var(--es-block-bg);
    padding: 16px;
    animation-name: slide;

    &:nth-child(1) {
        animation-duration: 0.5s;
    }

    &:nth-child(2) {
        animation-duration: 1s;
    }

    &:nth-child(3) {
        animation-duration: 1.5s;
    }

    &+& {
        margin-top: 20px;
    }
}

@keyframes slide {


    0% {
        transform: translateX(-100%);
    }

    80% {
        transform: translateX(20px);
    }

    100% {
        transform: translateX(0);
    }
}
</style>
