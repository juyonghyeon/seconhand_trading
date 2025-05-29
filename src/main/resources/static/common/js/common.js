/* window.alert를 SweetAlert2로 교체 */
window.alert = function(message) {
    Swal.fire({
      title: message,
      icon: "warning"
    });
};