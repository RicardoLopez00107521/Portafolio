<template>
  <div class="modal-container" v-if="visible">
    <div class="modal-content">
      <button class="close-button" @click="closeModal">✕</button>
      <h3>Agregar un nuevo miembro</h3>
      <form @submit.prevent="confirmAddMember">
        <label for="email">Correo electrónico del nuevo miembro:</label>
        <input type="email" v-model="newMemberEmail" id="email" required />
        <div class="modal-actions">
          <button type="button" @click="closeModal">Cancelar</button>
          <button type="submit">Agregar miembro</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AddMemberModal',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      newMemberEmail: ''
    };
  },
  methods: {
    closeModal() {
      this.$emit('close');
    },
    confirmAddMember() {
      this.$emit('add-member', this.newMemberEmail);
      this.newMemberEmail = '';
      this.closeModal();
    }
  }
}
</script>

<style scoped>
.modal-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.modal-content {
  background: #ffffff;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
  width: 400px;
  position: relative;
}

.close-button {
  position: absolute;
  top: 15px;
  right: 15px;
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
}

.modal-content h3 {
  margin-bottom: 20px;
  color: var(--title-color);
  font-size: 18px;
  font-family: var(--primary-font);
  text-align: left;
}

label {
  display: block;
  margin-bottom: 10px;
  font-size: 14px;
  color: var(--title-color);
  font-family: var(--primary-font);
  text-align: left;
}

input[type="email"] {
  width: 100%;
  padding: 10px;
  margin-bottom: 20px;
  border: 1px solid #dcdcdc;
  border-radius: 5px;
  color: var(--title-color);
  font-size: 14px;
  font-family: var(--primary-font);
  border-color: var(--title-color);
}

input:focus {
  outline-color: var(--primary-color);
}

.modal-actions {
  display: flex;
  justify-content: space-evenly;
  gap: 10px;
}

button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button[type="button"] {
  background-color: var(--bg-color);
  color: var(--title-color);
  font-size: 14px;
  font-family: var(--primary-font);
  font-weight: 600;
}

button[type="button"]:hover {
  background-color: #E0E1E5;  
}

button[type="submit"] {
  background-color: var(--primary-color);
  color: white;
  font-size: 14px;
  font-family: var(--primary-font);
  font-weight: 600;
}

button[type="submit"]:hover {
  background-color: #1d3a94;
}
</style>






