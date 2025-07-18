<template>
  <div>
    <div class="card-admin">
      <img :src="src" class="card-img" alt="Vigilant Image">
      <h5 class="card-title">{{name}}</h5>
      <p class="card-email">{{email}}</p>
      <button @click="showDeleteModal">Eliminar</button>
    </div>
    <DeleteGuardModal
      :visible="isDeleteModalVisible"
      @close="hideDeleteModal"
      @delete-guard="confirmDelete"
      :vigilantId="id"
    />
  </div>
</template>

<script>
import DeleteGuardModal from './DeleteGuardModal.vue';

export default {
  name: 'CardAdmin',
  components: {
    DeleteGuardModal
  },
  props: ['src', 'name', 'email', 'id'],
  data() {
    return {
      isDeleteModalVisible: false
    };
  },
  methods: {
    showDeleteModal() {
      this.isDeleteModalVisible = true;
    },
    hideDeleteModal() {
      this.isDeleteModalVisible = false;
    },
    confirmDelete() {
      this.$emit('remove');
      this.hideDeleteModal();
    }
  }
};
</script>



<style scoped>
.card-admin {
  width: 300px;
  height: 280px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 10px; 
  border: 1px solid transparent; 
  background-color: #ffffff;
  gap: 10px;
  box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1); 
  margin-bottom: 20px;
  padding: 20px;
}

.card-img {
  width: 100px;
  height: 100px;
}

.card-title {
  color: var(--title-color);
  font-size: 18px;
  font-family: var(--primary-font);
}

.card-email {
  font-size: var(--menu-items-size);
  color: var(--title-color);
}

button {
  background-color: var(--primary-color);
  font-size: 12px;
  color: white;
  font-family: var(--primary-font);
  font-weight: 600;
  padding: 5px 30px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}

button:hover {
  background-color: #1d3a94;
}
</style>

