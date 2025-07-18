<template>
    <div v-if="visible" :class="['toast', type]" @click="hide">
      {{ message }}
    </div>
  </template>
  
  <script>
  export default {
    name: 'Toast',
    props: {
      message: {
        type: String,
        required: true
      },
      type: {
        type: String,
        default: 'info'
      }
    },
    data() {
      return {
        visible: false,
        timeoutId: null
      };
    },
    methods: {
      show() {
        this.visible = true;
        this.timeoutId = setTimeout(this.hide, 3000);
      },
      hide() {
        this.visible = false;
        clearTimeout(this.timeoutId);
      }
    },
    watch: {
      message: {
        immediate: true,
        handler() {
          if (this.message) {
            this.show();
          }
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .toast {
    position: fixed;
    top: 20px;
    right: 20px;
    padding: 15px 20px;
    border-radius: 5px;
    color: var(--white-color);
    z-index: 1000;
    cursor: pointer;
    transition: opacity 0.5s ease;
    font-family: var(--primary-font);
  }
  
  .toast.info {
    background-color: var(--primary-color); /* Azul */
  }
  
  .toast.success {
    background-color: #22A892; /* Verde, ajustado directamente ya que no está definido en :root */
  }
  
  .toast.error {
    background-color: #dc3545; /* Rojo, se mantiene igual o puedes agregar una variable en :root si lo prefieres */
  }
  </style>