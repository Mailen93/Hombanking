const {createApp} = Vue

createApp({
    data(){
        return{
        email: '',
        password: '',
        error: '',
        firstName: '',
        lastName: '',
        cardType: '',
        cardColor: '',
    };
},
    methods: {
        login() {
            console.log(this.login)
            axios.post('/api/login', `email=${this.email}&password=${this.password}`, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })

            .then(response => {
                window.location.href = '/web/accounts.html';
            })
            .catch(error => {
                this.error = error.response.data.message;
                    Swal.fire({
                        title: 'Missing data',
                        showClass: {
                          popup: 'animate__animated animate__fadeInDown'
                        },
                        hideClass: {
                          popup: 'animate__animated animate__fadeOutUp'
                        }
                      })
            });
        },
        register() {
            axios.post('/api/clients', `first=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`, {
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                })
            .then(()=>{
                const Toast = Swal.mixin({
                    toast: true,
                    position: "top-end",
                    showConfirmButton: false,
                    timer: 3000,
                    timerProgressBar: true,
                    didOpen: (toast) => {
                      toast.addEventListener("mouseenter", Swal.stopTimer);
                      toast.addEventListener("mouseleave", Swal.resumeTimer);
                    },
                  });
      
                  Toast.fire({
                    icon: "success",
                    title: "Signed in successfully",
            })
            .then(response => {
               this.login();
            })
            })
            .catch(error => {
                this.error = error.response.data.message;
                Swal.fire({
                    title: 'Missing data',
                    showClass: {
                      popup: 'animate__animated animate__fadeInDown'
                    },
                    hideClass: {
                      popup: 'animate__animated animate__fadeOutUp'
                    }
                  })
            })
    },
 
}
}).mount('#app')