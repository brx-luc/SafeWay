# SafeWay
SafeWay ist eine App, welche innerhalb von drei Tagen im Rahmen des Moduls 335 programmiert wurde.
Mit dieser App kann man seinen Standort mit ausgewählten Kontakten teilen.

## Reflexion
Die Idee von SafeWay war, dass ein User Nummern und Namen von Kontakten speichern kann, welche in einer SQLite Datenbank abgelegt werden sollen.
Diese Kontakte sollten ausserdem bearbeitet und gelöscht werden können. Durch Schütteln des Geräts wird der aktuelle Standort mit den gespeicherten Kontakten per SMS geteilt. Der Standort wird auch alle 5 Minuten aktualisiert und ebenfalls per SMS gesendet. Sobald man das Teilen beenden will, kann man auf den Button "I arrived safely" klicken. Durch das Klicken dieses Buttons wird eine letzte SMS mit dem Inhalt "I arrived safely" an die Kontakte gesendet.

Nach drei Tagen programmieren mit Android Studio ist das der aktuelle Stand des Projekts SafeWay:
Die SQLite Datenbank wurde erfolgreich implementiert. Der User kann sowohl neue Kontakte mit Vor- und Nachnamen und Nummer speichern, wie auch diese gespeicherten Nummern bearbeiten und löschen. Die Änderungen werden erfolgreich aktualisiert und in der EditNumberActivity aufgelistet. Am Wochenende konnte ich die sendSMS Funktion mit dem Android Gerät meiner Mutter testen (ich selbst besitze ein iPhone). Es wurden tatsächlich mehrere SMS mit den Koordinaten des Standorts gesendet, jedoch stimmt der Abstand der SMS nicht und nach den ersten 4-5 Nachrichten, werden keine Nachrichten mehr gesendet.

Ich habe nicht damit gerechnet, dass die App so gut funktioniert (auch wenn sie noch nicht so funktioniert, wie ich es gerne hätte), aber für die fünf Tage, die wir uns mit diesem Thema befasst haben, denke ich, dass ich ein ganz passables Programm auf die Beine gestellt habe. Das Programmieren hat mir vor allem darum Spass gemacht, weil man das Ergebnis und die Änderungen direkt im GUI sehen konnte und es nicht nur ein Konsolen-Programm ist. 
Was ich das nächste Mal aber bestimmt anders machen würde, ist, dass ich mich im vorherein ein bisschen mehr damit auseinandersetze, mit welchen Technologien meine Idee umgesetzt werden kann und welche dieser Technologien am besten umzusetzen ist. Man findet zwar viele Tutorials und Beispiele, wie man gewisse Sachen umsetzen kann, man muss aber auch berücksichtigen, dass sich nicht jedes Beispiel für die Umsetzung der eigenen Idee eignet. 
Fürs nächste Mal wenn ich eine Android Applikation programmiere, sollte ich mir auch ein Android Gerät organisieren, mit dem ich Funktionen wie das Senden von SMS direkt testen kann. Natürlich hätte ich auch eine Cross-Platform-App entwickeln können, ich denke aber, dass ich zu wenig Zeit gehabt hätte, um mich darüber zu informieren wie man diesen App-Typ programmieren muss.
