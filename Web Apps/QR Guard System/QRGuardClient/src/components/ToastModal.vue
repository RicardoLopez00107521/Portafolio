<template>
  <v-snackbar
    :timeout="timeout"
    :color="color"
    :rounded="rounded"
    v-model="showSnackbar" 
  >
    <strong>{{ message }}</strong>
  </v-snackbar>
</template>

<script>
export default {
  props: {
    message: {
      type: String,
      required: true
    },
    timeout: {
      type: Number,
      default: 2000
    },
    color: {
      type: String,
      default: 'blue-grey'
    },
    rounded: {
      type: String,
      default: 'pill'
    }
  },
  data() {
    return {
      showSnackbar: false // Inicialmente oculto
    };
  }
};
</script>


  