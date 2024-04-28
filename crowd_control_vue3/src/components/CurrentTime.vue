<template>
    <span class="timer">{{ currentTime }}</span>
</template>

<script>
    export default {
        data() {
            return {
                currentTime: "",
            };
        },
        created() {
            this.updateTime();
            setInterval(this.updateTime, 1000);
        },
        methods: {
            updateTime() {
                const date = new Date();
                const hours = this.padZero(date.getHours());
                const minutes = this.padZero(date.getMinutes());
                const seconds = this.padZero(date.getSeconds());
                this.currentTime = `${hours}:${minutes}:${seconds}`;
            },
            padZero(num) {
                return num < 10 ? '0' + num : num.toString();
            },
        },
    };
</script>

<style scoped>
    .timer {
        margin: 0 20px;
        font-weight: bold;
    }
</style>