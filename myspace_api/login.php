<?php
require "con.php";

$email = $_POST["email"];
$password = $_POST["psw"];

// $email = "fode@gmail.com";
// $password = "passer";
$isValidEmail = filter_var($email, FILTER_VALIDATE_EMAIL);
if($conn) {
	if($isValidEmail == false){
		echo "L'Email n'est pas valide.";
	}
	else {
		$sqlCheckEmail = "SELECT * FROM  `users` WHERE `email` LIKE '$email' ";
		$emailQuery = mysqli_query($conn, $sqlCheckEmail);
		if(mysqli_num_rows($emailQuery) > 0) {
			$sqlCheckLogin = "SELECT * FROM  `users` WHERE `email` LIKE '$email' AND `password` LIKE '$password' ";
			$sqlLogin = mysqli_query($conn, $sqlCheckLogin);
			if(mysqli_num_rows($sqlLogin) > 0) {
				echo "Connexion réussie !";
			}
			else {
				echo "Mot de passe incorrect.";
			}
		}
		else {
			echo "Cet Email n'existe pas !!!";
		}
	}
	
}
else {
	echo "Erreur Connexion !!!";
}

?>