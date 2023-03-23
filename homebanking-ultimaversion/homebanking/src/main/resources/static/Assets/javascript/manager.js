const { createApp } = Vue;

createApp({
  data() {
    return {
      data: undefined,
      clients: [],
      selectClient: { firstName: "", lastName: "", email: "" },
      newClient: { firstName: "", lastName: "", email: "" },
      editMode: false,
      name: "",
      payments: "",
      maxAmount: "",
      iva: ""
    };
  },
  created() {
    this.loadData();
  },
  methods: {
    loadData() {
      axios
        .get("/rest/clients")
        .then((response) => {
          (this.data = response),
            (this.clients = this.data.data._embedded.clients);
          console.log(this.clients);
        })
        .catch((err) => console.log(err));
    },
    addLoan() {
      axios
        .post(
          "/api/loans/admin",
          `name=${this.name}&maxAmount=${this.maxAmount}&payments=${this.payments}&iva=${this.iva}`,
          {
            headers: {
              "Content-Type": "application/x-www-form-urlencoded",
            },
          }
        )
        .then(() => {
          Swal.fire({
            icon: "success",
            timer: 2000,
          }).then(() => {
            location.href = "/web/managerLoan.html";
          });
        })
        .catch((error) => {
          this.loginError = error.response;
          Swal.fire({
            title:
              `${this.loginError.data}...` +
              " " +
              `(error: ${this.loginError.status})`,
            text: "¡Corrige tus datos y vuelve a intentarlo!",
            icon: "error",
            dangerMode: true,
          });
        });
    },
    addClient() {
      if (
        this.newClient.firstName &&
        this.newClient.lastName &&
        this.newClient.email
      ) {
        axios.post("/rest/clients", this.newClient).then((response) => {
          this.loadData();
          this.refreshForm();
        });
      } else {
        alert("Please complete the empty fields");
      }
    },
    deleteClient(client) {
      axios.delete(client._links.client.href).then((response) => {
        this.loadData();
      });
    },
    editClient() {
      this.selectClient = this.newClient;
      axios.put(this.newClient.id, this.selectClient).then((response) => {
        this.loadData();
        this.editMode = false;
        this.refreshForm();
      });
    },
    startEditing(client) {
      this.newClient.firstName = client.firstName;
      this.newClient.lastName = client.lastName;
      this.newClient.email = client.email;
      this.newClient.id = client._links.client.href;
      this.editMode = true;
    },
    refreshForm() {
      this.newClient = { firstName: "", lastName: "", email: "" };
    },
  },
}).mount("#app");
