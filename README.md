# The Co-Evolution Of Land Use And Road Networks

## Bibliographic Information

- Row ID: `paper-2007-04`
- Year: 2007
- Authors: David Levinson, Feng Xie, and Shanjiang Zhu
- Venue: Transportation and Traffic Theory 2007, Emerald, pp. 839-859
- Public manuscript pointer: https://hdl.handle.net/11299/179950
- Citation: Levinson, D., Xie, F., and Zhu, S. (2007). The co-evolution of land use and road networks. In Transportation and Traffic Theory 2007, Emerald, pp. 839-859.

## Package Status

This package is intended for `READY-TO-UPLOAD/PUBLIC`. The paper is simulation/model based and does not use human-subjects microdata. The package now includes the local SIGNAL model source and compact input/configuration files that logically match the paper's described simulator.

Before public GitHub release, add an explicit source-code license. No privacy restriction was found in the staged source or model-input files.

## Package Contents

- `paper/ISTTT-LevinsonXieZhu.pdf`: local reference copy for audit convenience.
- `code/signal_source_original/`: original Java source for SIGNAL 1.8.1, including the applet interface, network dynamics, travel generation, trip distribution, traffic assignment, investment, graph, and stack classes.
- `code/signal_runnable_2021/`: compact 2021 runnable wrapper files (`SIGNAL.jar`, `SIGNAL.exe`, `SIGNAL.xml`) copied from the local `SIGNAL.zip` update, without duplicating input files.
- `data/signal_inputs/`: model inputs and batch configuration needed to generate SIGNAL outputs, including grid networks, coordinate files, TAZ information, OD cost data, and `Batch.txt`.
- `documentation/SIGNAL_development_log.txt`: original development notes copied from the local SIGNAL folder.
- `documentation/PAPER_FIRST_VALIDATION.md`: paper-first audit note linking the paper claims to the staged files.
- `documentation/SOURCE_BOUNDARY.md`: inclusion and exclusion rationale.
- `metadata/PACKAGE_MANIFEST.csv`: file-level manifest with hashes and package roles.
- `metadata/EXCLUDED_LOCAL_FILES.csv`: local files and categories deliberately excluded from this archive boundary.

## Evidence Match

The paper describes SIGNAL, a simulator integrating travel demand, road investment, accessibility, and land-use dynamics. The staged code directly implements that model family: `Signal.java` identifies the application as "SIGNAL1.8.0: Simulator of Integrated Growth of Network And Land use"; `NetworkDynamics.java` wires together `TGeneration`, `DijkstrasAlgo`, `TDistribution`, `TAssignment`, and `Investment`; `DirectedGraph.java` stores link capacity, speed, flow, BPR travel time, generalized cost, and TAZ population/employment fields; and the land-use update code computes accessibility to employment and residence and updates population/employment distributions.

This is a stronger match than the older `SONG1-Code` / `NDApplication` predecessor, which is relevant background for network dynamics but does not by itself cover the full SIGNAL land-use co-evolution paper.

## Excluded Material

The local paper folder contains drafts, formatted copies, contributor agreements, conference programs, travel documents, presentations, Keynote bundles, and generated figure images. Those are not data/code archive assets and were excluded.

The SIGNAL source folder also contains large `signal1.8.0-*.txt` output files, totaling roughly 320 MB. They appear to be generated experiment outputs, not inputs. They are excluded because the user wants inputs/code sufficient to generate outputs rather than copying bulky generated results into GitHub.

The source was inspected but not compiled in this pass because this Mac currently has no local Java runtime installed.

<!-- package-hardening-status:start -->
## Package Hardening Status

Generated: 2026-05-21 20:04:48 AEST

- Pipeline: `READY-TO-UPLOAD/PUBLIC`
- Sidecars added/updated: `PACKAGE_STATUS.md`, `PACKAGE_MANIFEST.csv`, `LICENSE_STATUS.md`.
- Paper reference copies are for local audit convenience and are not public-upload assets without rights review.
- Final GitHub upload should use the manifest include statuses and the license-status note.
<!-- package-hardening-status:end -->
